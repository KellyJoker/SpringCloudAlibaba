package org.example.mybatisplus.util;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

/**
 * @Description mybatis-plus的逆向生成
 * @Author danxiaodong
 * @Date 2023/7/21 14:26
 **/
public class MybatisPlusGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/springboot_mybatis?useUnicode=true&useSSL=false&characterEncoding=utf8", "root", "root1234")
                .globalConfig(builder -> {
                    builder.author("danxiaodong") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(System.getProperty("user.dir")+"/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("org.example") // 设置父包名
                            .moduleName("mybatisplus") // 设置父包模块名
                            // .service()  // 设置自定义service路径,不设置就是默认路径
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") +"/src/main/resources/mapper/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user_info") // 设置需要生成的表名
                            //.addTablePrefix("t_", "c_")
                    ; // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
