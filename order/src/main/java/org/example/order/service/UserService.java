package org.example.order.service;

import org.example.order.entity.pojo.User;
import org.example.order.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/20 19:05
 **/
public interface UserService {
    public List<User> qryAll();
}
