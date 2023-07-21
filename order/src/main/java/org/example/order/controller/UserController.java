package org.example.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.order.entity.dto.UserDTO;
import org.example.order.entity.pojo.UserPO;
import org.example.order.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/20 18:59
 **/
@Api(tags = "用户信息")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/qryAll")
    @ApiOperation("查询所有用户")
    public List<UserDTO> qryAll(){
        return userService.qryAll();
    }

    @ApiOperation("添加用户信息")
    @PostMapping("/add")
    public String add(@RequestBody UserPO userPO){
        return userService.add(userPO);
    }
}
