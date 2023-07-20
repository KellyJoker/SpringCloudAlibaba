package org.example.order.service;

import org.example.order.entity.pojo.User;
import org.example.order.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/20 19:13
 **/
@Component
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> qryAll() {
        return userMapper.selectAll();
    }
}
