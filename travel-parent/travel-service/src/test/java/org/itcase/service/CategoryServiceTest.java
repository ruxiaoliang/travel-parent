package org.itcase.service;

import lombok.extern.log4j.Log4j2;
import org.itcase.basic.TestConfig;
import org.itcase.req.CategoryVo;
import org.itcase.req.UserVo;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Log4j2
public class CategoryServiceTest extends TestConfig {

    @Before
    public void before(){
        UserVo userVo = UserVo.builder()
                .username("admin")
                .password("pass")
                .build();
        UserVo userVoResult = userService.loginUser(userVo);
    }


    @Test
    public void testFindAllCategory(){
        log.info("testFindAllCategory----开始");
        List<CategoryVo> allCategory = categoryService.findAllCategory();
        log.info("testFindAllCategory----结束:{}",allCategory);
    }
}
