package org.itcase.service.impl;

import org.itcase.config.SnowflakeIdWorker;
import org.itcase.constant.RedisConstant;
import org.itcase.mapper.AffixMapper;
import org.itcase.pojo.Affix;
import org.itcase.pojo.AffixExample;
import org.itcase.req.AffixVo;
import org.itcase.service.AffixService;
import org.itcase.service.RedisCacheService;
import org.itcase.utils.BeanConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description：文件上传实现
 */
@Service
public class AffixServiceImpl implements AffixService {

    @Autowired
    private AffixMapper affixMapper;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private RedisCacheService redisCacheService;

    @Value("upLoad.pathRoot")
    String pathRoot;

    @Value("upLoad.webSite")
    String webSite;

    @Override
    public AffixVo upLoad(MultipartFile multipartFile, AffixVo affixVo) throws IOException {
        //判断文件是否为空
        if (multipartFile==null){
            return null;
        }
        String businessType = affixVo.getBusinessType();
        //关联业务
        affixVo.setBusinessType(businessType);
        //原始上传的文件名称aaa.jpg
        String originalFilename = multipartFile.getOriginalFilename();
        //后缀名称.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        affixVo.setSuffix(suffix);
        //文件名称
        String fileName = String.valueOf(snowflakeIdWorker.nextId());
        affixVo.setFileName(fileName);
        //构建访问路径
        String pathUrl = businessType+"/"+fileName+suffix;
        //判断业务类型的文件夹是否存在
        File file = new File(pathRoot+businessType);
        //文件夹不存在则创建
        if (!file.exists()){
            file.mkdir();
        }
        file = new File(pathRoot+pathUrl);
        multipartFile.transferTo(file);
        pathUrl = webSite+pathUrl;
        affixVo.setPathUrl(pathUrl);
        affixMapper.insert(BeanConv.toBean(affixVo, Affix.class));
        return affixVo;
    }

    @Override
    public Boolean bindBusinessId(AffixVo affixVo) {
        Affix affix = BeanConv.toBean(affixVo, Affix.class);
        int times = affixMapper.updateByPrimaryKeySelective(affix);
        boolean flag = times > 0;
        if (flag){
            //更新删除缓存
            String key = RedisConstant.AFFIXSERVICE_FINDAFFIXBYBUSINESSID +affixVo.getBusinessId();
            redisCacheService.deleListCache(key);
        }
        return flag;
    }

    @Override
    public List<AffixVo> findAffixByBusinessId(AffixVo affixVo) {
        /*AffixExample example = new AffixExample();
        example.createCriteria().andBusinessIdEqualTo(affixVo.getBusinessId());
        List<Affix> affixes = affixMapper.selectByExample(example);
        return BeanConv.toBeanList(affixes, AffixVo.class);*/
        //为每个主业务添加缓存
        String key = RedisConstant.AFFIXSERVICE_FINDAFFIXBYBUSINESSID +affixVo.getBusinessId();
        return redisCacheService.listCache(()->{
            AffixExample example = new AffixExample();
            example.createCriteria().andBusinessIdEqualTo(affixVo.getBusinessId());
            List<Affix> affixes = affixMapper.selectByExample(example);
            return BeanConv.toBeanList(affixes, AffixVo.class);
        }, key);
    }
}
