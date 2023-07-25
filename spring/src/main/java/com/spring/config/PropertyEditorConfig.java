package com.spring.config;

import com.spring.entity.User;
import com.spring.util.String2UserConverter;
import com.spring.util.String2UserPropertyEditor;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;

import java.beans.PropertyEditor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/24 23:44
 **/
@Configuration
public class PropertyEditorConfig {
    /**
     * 要如何使用类型转换工具呢？
     * 方法是将其注册到spring容器中，spring会自动找到该配置进行解析
     * spring在解析属性@Value 的值是一个字符串， 属性的类型又是一个User，spring就知道需要将字符串转化为User对象。而spring自身又没有
     * 提供这个转化器，此时会去看用户有没有提供将字符串转为User对象的转化器，若有，则执行该转化器的逻辑，将字符串转为User对象。
     * @return
     */
    @Bean
    public CustomEditorConfigurer customEditorConfigurer(){
        CustomEditorConfigurer configurer = new CustomEditorConfigurer();
        //map表示spring可以支持多个类型转换器
        Map<Class<?>, Class<? extends PropertyEditor>> propertyEditorMap = new HashMap<>();

        // 表示String2UserPropertyEditor可以将String转化成User类型，在Spring源码中，如果发现当前对象是String，
        // 而需要的类型是User，就会使用该PropertyEditor来做类型转化
        propertyEditorMap.put(User.class, String2UserPropertyEditor.class);
        configurer.setCustomEditors(propertyEditorMap);
        return configurer;
    }

    /**
     *
     * @return
     */
    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        factoryBean.setConverters(Collections.singleton(new String2UserConverter()));
        return factoryBean;
    }
}
