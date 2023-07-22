package org.example.mybatisplus.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.mybatisplus.entity.UserInfo;
import org.example.mybatisplus.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author danxiaodong
 * @since 2023-07-21
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    IUserInfoService userInfoService;

    @ApiOperation("查询所有用户")
    @GetMapping("/getUser")
    public List<UserInfo> getUser(){
         return userInfoService.list();
    }
}
