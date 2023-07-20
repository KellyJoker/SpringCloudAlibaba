package org.example.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.order.entity.pojo.User;
import org.example.order.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping ("/qryAll")
    @ApiOperation("查询所有用户")
    public List<User> qryAll(){
        return userService.qryAll();
    }
}
