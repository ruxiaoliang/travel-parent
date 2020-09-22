package org.itcase.service.impl;

import org.itcase.constant.RedisConstant;
import org.itcase.mapper.CategoryMapper;
import org.itcase.pojo.Category;
import org.itcase.pojo.CategoryExample;
import org.itcase.req.CategoryVo;
import org.itcase.service.CategoryService;
import org.itcase.service.RedisCacheService;
import org.itcase.session.SubjectUserContext;
import org.itcase.utils.BeanConv;
import org.itcase.utils.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    SubjectUserContext subjectUserContext;

    @Override
    public List<CategoryVo> findAllCategory() {
        /*//传入  无
        //获取  example形式对象
        CategoryExample categoryExample = new CategoryExample();
        List<Category> categories = categoryMapper.selectByExample(categoryExample);
        //传出
        if (!EmptyUtil.isNullOrEmpty(categories))  return BeanConv.toBeanList(categories,CategoryVo.class);
        return null;*/
        return redisCacheService.listCache(()->{
            CategoryExample example = new CategoryExample();
            List<Category> categories = categoryMapper.selectByExample(example);
            if (!EmptyUtil.isNullOrEmpty(categories)){
                return BeanConv.toBeanList(categories, CategoryVo.class);
            }
            return null;
        }, RedisConstant.CATEGORYSERVICE_FINDALLCATEGORY);
    }
}
