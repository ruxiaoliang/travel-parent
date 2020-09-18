package org.itcase.service;

import lombok.extern.log4j.Log4j2;
import org.itcase.basic.TestConfig;
import org.itcase.req.UserVo;
import org.junit.Test;

import java.util.Date;

@Log4j2
public class UserServiceTest extends TestConfig {

    @Test
    public void testRegisterUser(){
        Boolean flag = userService.registerUser(UserVo.builder()
                .username("yunWuYue")
                .password("123")
                .birthday(new Date())
                .email("123@12.com")
                .sex("男")
                .telephone("1212121212")
                .realName("云无月")
                .build());

        userService.isLogin();

        log.info("注册结果为" + flag);
    }


    @Test
    public void testLoginUser(){

            UserVo userVoResult = userService.loginUser(UserVo.builder()
                .username("yunWuYue")
                .password("123")
                .build());
            log.info("testLoginUser----通过--->{}",userVoResult);
    }

    @Test
    public void testLoginOutUser(){
        UserVo userVoResult = userService.loginUser(UserVo.builder()
                .username("yunWuYue")
                .password("123")
                .build());
        log.info("testLoginUser----通过--->{}",userVoResult.toString());

        log.info("testLoginOutUser----开始");
        userService.loginOutUser();
    }

    @Test
    public void testIsLogin(){
        UserVo userVoResult = userService.loginUser(UserVo.builder()
                .username("yunWuYue")
                .password("123")
                .build());
        log.info("testLoginUser----通过--->{}",userVoResult.toString());

        log.info("testLoginOutUser----开始");
        Boolean flag = userService.isLogin();
        log.info("testIsLogin----通过--->{}",flag);
    }
}
