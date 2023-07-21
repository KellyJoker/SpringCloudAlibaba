package org.example.order.service;

import org.example.order.entity.dto.UserDTO;
import org.example.order.entity.pojo.UserPO;
import org.example.order.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public List<UserDTO> qryAll() {
        List<UserPO> users = userMapper.selectAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (UserPO user : users) {
            UserDTO userDTO = new UserDTO();
            if (user.getGender()==1){
                userDTO.setGender("男");
            }else{
                userDTO.setGender("女");
            }
            userDTO.setUsername(user.getUsername());
            userDTO.setAge(String.valueOf(user.getAge()));
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    @Override
    public String add(UserPO userPO) {
        String msg = "插入数据成功！";
        try {
            int insert = userMapper.insert(userPO);
        }catch (Exception e){
            msg = "插入数据失败！";
        }
        return msg;
    }
}
