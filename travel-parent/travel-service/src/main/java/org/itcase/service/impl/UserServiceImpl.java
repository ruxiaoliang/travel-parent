package org.itcase.service.impl;

import org.itcase.mapper.UserMapper;
import org.itcase.pojo.User;
import org.itcase.pojo.UserExample;
import org.itcase.req.UserVo;
import org.itcase.service.UserService;
import org.itcase.session.SubjectUser;
import org.itcase.session.SubjectUserContext;
import org.itcase.utils.BeanConv;
import org.itcase.utils.MD5Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SubjectUserContext subjectUserContext;

    @Override
    public Boolean registerUser(UserVo userVo) {
        //首先进行对象转换
        User user = BeanConv.toBean(userVo, User.class);
        //对密码进行加密
        Objects.requireNonNull(user).setPassword(MD5Coder.md5Encode(user.getPassword()));
        //然后将转换后的PO传入Mapper进行数据库处理后对返回值进行判断
        return userMapper.insert(user) > 0;
    }

    @Override
    public UserVo loginUser(UserVo userVo) {
        //新建一个用户类型的范例
        UserExample userExample = new  UserExample();
        //拼接出需要的对象模型
        userExample.createCriteria()
                .andUsernameEqualTo(userVo.getUsername())
                .andPasswordEqualTo(MD5Coder.md5Encode(userVo.getPassword()));
        //对查询后的数据进行处理
        List<User> userList = userMapper.selectByExample(userExample);

        UserVo resultVo = null;
        if (userList.size() == 1){
            //获取这个对象并转成本层的对象
            resultVo = BeanConv.toBean(userList.get(0), UserVo.class);

            //生成唯一标识并放入到token中
            String token = UUID.randomUUID().toString();
            Objects.requireNonNull(resultVo).setToken(token);
            //转换成collector层的对象并放进session中
            SubjectUser subjectUser = BeanConv.toBean(resultVo, SubjectUser.class);
            subjectUserContext.createdSubject(token,subjectUser);
        }
        return resultVo;
    }

    @Override
    public void loginOutUser() {
        subjectUserContext.deleteSubject();
    }

    @Override
    public Boolean isLogin() {
        return subjectUserContext.existSubject();
    }
}
