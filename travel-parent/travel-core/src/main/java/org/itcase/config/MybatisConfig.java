package org.itcase.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.plugin.Interceptor;
import org.itcase.interceptor.PrimaryKeyInterceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description：声明mybatis的配置
 */
@Configuration
//读取外部配置文件
@PropertySource(value = "classpath:db.properties")
@MapperScan(basePackages = {"org.itcase.mapper","org.itcase.mapperExt"},sqlSessionFactoryRef = "sqlSessionFactoryBean")
public class MybatisConfig {

    @Value("${dataSource.driverClassName}")
    private String driverClassName;

    @Value("${dataSource.url}")
    private String url;

    @Value("${dataSource.username}")
    private String username;

    @Value("${dataSource.password}")
    private String password;

    @Value("${seq.workerId}")
    private String workerId;

    @Value("${seq.datacenterId}")
    private String datacenterId;

    /**
     * @Description 数据源配置
     * 细节：默认不指定名称使用当前方法名为bean的名称
     */
    @Bean("druidDataSource")
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * @Description 配置事务管理器
     * 细节：名称必须是：transactionManager，如果更换需要在使用时指定
     */
    @Bean("transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("druidDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * @Description 配置会话管理器
     */
    @Bean("sqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("druidDataSource") DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //指定数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        //指定对应的别名
        sqlSessionFactoryBean.setTypeAliasesPackage("org.itcase.pojo");
        //指定核心xml的配置
//        try {
//            sqlSessionFactoryBean.setMapperLocations(
//                    new PathMatchingResourcePatternResolver()
//                    .getResources("sqlMapper/*Mapper.xml"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //指定高级配置：驼峰、是否返回主键、缓存、日志
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(Log4j2Impl.class);
        sqlSessionFactoryBean.setConfiguration(configuration);

        //插件
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{initPageInterceptor(),initPrimaryKeyInterceptor()});

        return sqlSessionFactoryBean;
    }

    /**
     * @Description 分页插件
     * PageInterceptor  分页插件拦截器
     */
    @Bean
    public PageInterceptor initPageInterceptor(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        // 数据库方言(对哪个数据库进行分页)
        properties.setProperty("helperDialect", "mysql");
        // 分页偏移量设置(默认值为false,需要开启)
        // 开启后可以使用当前页页码与每页显示条数进行分页
        properties.setProperty("offsetAsPageNum", "true");
        // 开启分页时查询总数量
        properties.setProperty("rowBoundsWithCount", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }


    /**
     * @Description 雪花算法支持
     * 从db.properties中获取参数
     */
    @Bean
    public SnowflakeIdWorker snowflakeIdWorker(){
        return new SnowflakeIdWorker(Integer.parseInt(workerId), Integer.parseInt(datacenterId));
    }

    /**
     * @Description 主键生成拦截
     * 创建拦截器对象，交由IOC容器管理
     */
    @Bean
    public PrimaryKeyInterceptor initPrimaryKeyInterceptor(){
        PrimaryKeyInterceptor primaryKeyInterceptor =  new PrimaryKeyInterceptor();
        Properties properties = new Properties();
        //primaryKey自定义的键名称   id需要替换的字段
        properties.setProperty("primaryKey", "id");
        primaryKeyInterceptor.setProperties(properties);
        primaryKeyInterceptor.setSnowflakeIdWorker(snowflakeIdWorker());
        return primaryKeyInterceptor;
    }

}
