package org.example.mybatisplus.service.impl;

import org.example.mybatisplus.entity.UserInfo;
import org.example.mybatisplus.mapper.UserInfoMapper;
import org.example.mybatisplus.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author danxiaodong
 * @since 2023-07-21
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
