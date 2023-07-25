package com.spring.util;

import com.spring.entity.User;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

/**
 * @Description 类型转化器，将@Value注解的字符串值转为User对象
 * 注意⚠️：需要继承 PropertyEditorSupport 类和实现 PropertyEditor 接口，这些是JDK提供的类型转换工具
 * @Author danxiaodong
 * @Date 2023/7/24 23:08
 **/
public class String2UserPropertyEditor extends PropertyEditorSupport implements PropertyEditor {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        User user = new User();
        String[] split = text.split(",");
        user.setUsername(split[0]);
        user.setAge(split[1]);
        this.setValue(user);
    }
}
