package org.example.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description swagger接口文档配置类
 * @Author danxiaodong
 * @Date 2023/7/18 20:43
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 告诉 springfox 怎么去生成 swagger 所需要的规范数据
     * @return Docket
     */
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2) //生成swagger2规范文档
                .pathMapping("/") //设置哪些接口会映射到swagger2文档中
                .select() //接口选择器
                //告诉swagger2在哪些包下的接口需要生成swagger文档
                .apis(RequestHandlerSelectors.basePackage("org.example.redis.controller"))
                //设置哪些接口生成在swagger文档上
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("SpringBoot整合Swagger")
                        .description("SpringBoot整合Swagger详细信息")
                        .version("1.0.0")
                        .contact(new Contact("SpringBoot-SpringCloudAlibaba", "www.google.com", "dxdsama@gmail.com"))
                        .build());
    }
}
