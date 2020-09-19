package org.itcase.service;

import org.itcase.req.CategoryVo;

import java.util.List;

/**
 * @Description 分类服务接口
 */
public interface CategoryService {

    /**
     * @Description 查询所分类
     */
    List<CategoryVo> findAllCategory();
}
