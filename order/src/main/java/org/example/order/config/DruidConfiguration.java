package org.example.order.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Description druid连接池配置
 * 注意⚠️：若pom文件中引入了 druid-spring-boot-starter ，则此配置类无须配置
 * @Author danxiaodong
 * @Date 2023/7/19 23:35
 **/
//@Configuration
public class DruidConfiguration {

    /*@Bean
    // 会绑定application.yml所有spring.datasourcek开头的配置绑定到DataSource
    @ConfigurationProperties(prefix =  "spring.datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

//    @Bean
//    public DataSource dataSource(DataSourceProperties properties){
//        //根据配置动态构建一个DataSource
//        return properties.initializeDataSourceBuilder().build();
//    }

    *//**
     * druid监控台
     * @return ServletRegistrationBean
     *//*
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 添加IP白名单
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
        servletRegistrationBean.addInitParameter("deny", "127.0.0.1");
        // 添加控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        // 是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    *//**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     *//*
    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }*/
}
