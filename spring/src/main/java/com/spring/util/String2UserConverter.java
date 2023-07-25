package com.spring.util;

import com.spring.entity.User;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @Description 类型转化器，将@Value注解的字符串值转为User对象
 * 注意⚠️：需要实现 ConditionalGenericConverter 接口，这是Spring提供的类型转换工具，它比PropertyEditor更强大。
 * @Author danxiaodong
 * @Date 2023/7/25 10:10
 **/
public class String2UserConverter implements ConditionalGenericConverter {
    /**
     * 定义适配的场景
     * 这里不仅仅可以将String类型的转换为User，也可以将其他类型的相互转换，功能强大
     * @param sourceType 待转换的类型
     * @param targetType 目标类型
     * @return
     */
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.getType().equals(String.class) && targetType.getType().equals(User.class);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, User.class));
    }

    /**
     * 转换逻辑
     * @param source
     * @param sourceType
     * @param targetType
     * @return
     */
    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        User user = new User();
        System.out.println(source);
        String[] split = String.valueOf(source).split(",");
        user.setUsername(split[0]);
        user.setAge(split[1]);
        return user;
    }
}
