package com.xiaodong;

import com.spring.CustomApplicationContext;
import com.xiaodong.service.UserInterface;

public class Test {

    public static void main(String[] args) {

        // 扫描--->创建单例Bean BeanDefinition BeanPostPRocess
        CustomApplicationContext applicationContext = new CustomApplicationContext(AppConfig.class);

        UserInterface userService = (UserInterface) applicationContext.getBean("userService");
        userService.test();
    }
}
