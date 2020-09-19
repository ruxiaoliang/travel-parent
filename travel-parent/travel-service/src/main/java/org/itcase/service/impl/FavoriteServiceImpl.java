package org.itcase.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.itcase.mapper.FavoriteMapper;
import org.itcase.mapper.RouteMapper;
import org.itcase.mapperExt.FavoriteMapperExt;
import org.itcase.mapperExt.RouteMapperExt;
import org.itcase.pojo.Favorite;
import org.itcase.pojo.FavoriteExample;
import org.itcase.pojo.Route;
import org.itcase.req.FavoriteVo;
import org.itcase.req.RouteVo;
import org.itcase.service.FavoriteService;
import org.itcase.session.SubjectUser;
import org.itcase.session.SubjectUserContext;
import org.itcase.utils.BeanConv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl  implements FavoriteService {

    @Autowired
    private SubjectUserContext subjectUserContext;

    @Autowired
    private FavoriteMapperExt favoriteMapperExt;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private RouteMapperExt routeMapperExt;

    @Autowired
    private RouteMapper routeMapper;

    @Override
    public PageInfo<RouteVo> findMyFavorite(FavoriteVo favoriteVo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        //这里是从数据库中直接查询出来的List集合
        List<Route> favoriteList = favoriteMapperExt.findFavoriteById(subjectUserContext.getSubject().getId());
        //然后将查询出来的集合放进PageInfo集合中
        PageInfo<Route> pageInfo = new PageInfo<>(favoriteList);
        PageInfo<RouteVo> pageInfoVo = new PageInfo<>();
        //进行对象拷贝，将产生的对象转换成要发送给collector层需要的对象
        BeanConv.toBean(pageInfo,pageInfoVo);

        //对象拷贝之后进行数据的拷贝
        pageInfoVo.setList(BeanConv.toBeanList(pageInfo.getList(),RouteVo.class));
        return pageInfoVo;
    }

    @Override
    public Boolean isFavorited(FavoriteVo favoriteVo) {

        if (!subjectUserContext.existSubject()) return false;

        SubjectUser subjectUser = subjectUserContext.getSubject();

        FavoriteExample favoriteExample = new FavoriteExample();
        //使用例子 根据两个id构造出一个对象，用于传输到持久层
        favoriteExample.createCriteria()
                .andRouteIdEqualTo(favoriteVo.getRouteId())
                .andUserIdEqualTo(subjectUser.getId());

        //根据构造出来的对象，查询后返回结果
        List<Favorite> favorites = favoriteMapper.selectByExample(favoriteExample);
        return favorites.size() > 0;
    }

    @Override
    public Integer addFavorite(FavoriteVo favoriteVo) {
        SubjectUser subjectUser = subjectUserContext.getSubject();

        //将收藏的用户ID 放进 favoriteVo中
        favoriteVo.setUserId(subjectUser.getId());
        //将传进来的数据放进favoriteVo中
        favoriteMapper.insert(BeanConv.toBean(favoriteVo,Favorite.class));

        //使用我们构建的favoriteVo对象进行插入操作
        Long flag = routeMapperExt.updateRouteCountById(favoriteVo.getRouteId());
        if (flag == 0) throw new RuntimeException("插入失败");
        //插入成功之后查询收藏数量
        Route route = routeMapper.selectByPrimaryKey(favoriteVo.getRouteId());
        return route.getAttentionCount();
    }
}
