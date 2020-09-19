package org.itcase.service;

import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.itcase.basic.TestConfig;
import org.itcase.req.FavoriteVo;
import org.itcase.req.RouteVo;
import org.itcase.req.UserVo;
import org.junit.Before;
import org.junit.Test;

@Log4j2
public class FavoriteServiceTest extends TestConfig {

    /**
     * @Description 模拟用户登录
     */
    @Before
    public void before(){
        UserVo userVo = UserVo.builder()
                .username("admin")
                .password("pass")
                .build();
        UserVo userVoResult = userService.loginUser(userVo);
    }

    @Test
    public void testAddFavorite(){
        log.info("testAddFavorite----开始");
        FavoriteVo favoriteVo = FavoriteVo.builder()
                .routeId(1L)
                .build();
        int flag = favoriteService.addFavorite(favoriteVo);
        log.info("testAddFavorite----结束:{}",flag);
    }

    @Test
    public void testFindMyFavorite(){
        log.info("testFindMyFavorite----开始");
        FavoriteVo favoriteVo = FavoriteVo.builder()
                .build();
        PageInfo<RouteVo> page = favoriteService.findMyFavorite(favoriteVo, 1, 2);
        log.info("testFindMyFavorite----结束：{}",page.toString());
    }

    @Test
    public void isFavorited(){
        log.info("isFavorited----开始");
        FavoriteVo favoriteVo = FavoriteVo.builder()
                .routeId(1L)
                .build();
        boolean flag = favoriteService.isFavorited(favoriteVo);
        log.info("isFavorited----结束:{}",flag);
    }

}
