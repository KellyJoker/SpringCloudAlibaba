package com.spring.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/24 21:24
 **/
@Configuration
public class LanguageConfig {
    /**
     * 有了这个Bean，你可以在你任意想要进行国际化的地方使用该MessageSource。
     * @return
     */
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
}
