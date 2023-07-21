package org.example.order.service;

import org.example.order.entity.dto.UserDTO;
import org.example.order.entity.pojo.UserPO;

import java.util.List;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/20 19:05
 **/
public interface UserService {
    List<UserDTO> qryAll();

    String add(UserPO userPO);
}
