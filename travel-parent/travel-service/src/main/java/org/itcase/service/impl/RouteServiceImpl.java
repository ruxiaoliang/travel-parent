package org.itcase.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.itcase.mapper.RouteMapper;
import org.itcase.pojo.Route;
import org.itcase.pojo.RouteExample;
import org.itcase.req.RouteVo;
import org.itcase.service.RouteService;
import org.itcase.utils.BeanConv;
import org.itcase.utils.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    RouteMapper routeMapper;

    @Override
    public Integer addRoute(RouteVo routeVo) {
        //传入 routeVo
        Route route = BeanConv.toBean(routeVo, Route.class);
        //获取
        //传出
        return routeMapper.insert(route);
    }

    @Override
    public Integer updateRoute(RouteVo routeVo) {
        //传入对象类型 routeVo
        Route route = BeanConv.toBean(routeVo, Route.class);
        //从数据库获取持久层对象
        //传出 基本类型
        return routeMapper.updateByPrimaryKey(route);
    }

    @Override
    public RouteVo findRouteById(RouteVo routeVo) {
        //传入 对象类型RouteVo 获取Id
        Long id = routeVo.getId();
        //从数据库获取持久层对象
        Route route = routeMapper.selectByPrimaryKey(id);
        //传出 对象类型RouteVo
        return BeanConv.toBean(route,RouteVo.class);
    }

    @Override
    public PageInfo<RouteVo> findRouteByPage(RouteVo routeVo, int pageNum, int pageSize) {
        //传入 对象类型pageNum   pageSize   routeVo
        PageHelper.startPage(pageNum, pageSize);
        //根据example转换为传输给持久层的对象 并获取返回的持久对象
        RouteExample example = new RouteExample();
        RouteExample.Criteria criteria = example.createCriteria();
        //多条件查询
        if (!EmptyUtil.isNullOrEmpty(routeVo.getCategoryId())){
            criteria.andCategoryIdEqualTo(routeVo.getCategoryId());
        }
        if (!EmptyUtil.isNullOrEmpty(routeVo.getRouteName())){
            criteria.andRouteNameLike("%"+routeVo.getRouteName());
        }
        if (!EmptyUtil.isNullOrEmpty(routeVo.getMinPrice())){
            criteria.andPriceGreaterThan(routeVo.getMinPrice());
        }
        if (!EmptyUtil.isNullOrEmpty(routeVo.getMaxPrice())){
            criteria.andPriceLessThan(routeVo.getMaxPrice());
        }
        List<Route> routes = routeMapper.selectByExample(example);
        //传出 将对象类型转为RouteVo
        PageInfo<Route> pageInfo = new PageInfo<>(routes);
        PageInfo<RouteVo> pageInfoVo = new PageInfo<>();
        //外部对象拷贝
        BeanConv.toBean(pageInfo, pageInfoVo);
        //结果集转换
        List<RouteVo> routeVoList = BeanConv.toBeanList(pageInfo.getList(), RouteVo.class);
        pageInfoVo.setList(routeVoList);
        return pageInfoVo;
    }
}
