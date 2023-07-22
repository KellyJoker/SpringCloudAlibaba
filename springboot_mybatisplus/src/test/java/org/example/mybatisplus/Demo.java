package org.example.mybatisplus;

import org.example.mybatisplus.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description 注意⚠️：测试类所在的包与启动类所在的包必须在同一个路径下，否则会报错！！！
 * @Author danxiaodong
 * @Date 2023/7/22 10:29
 **/
@SpringBootTest
public class Demo {
    @Autowired
    private User user;

    @Test
    public void test_01(){
        if (user instanceof InitializingBean){
            System.out.println("user类实现了InitializingBean。。。");
        }else{
            System.out.println("....");
        }
    }
}
