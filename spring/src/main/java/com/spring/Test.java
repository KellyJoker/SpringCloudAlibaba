package com.spring;

import com.spring.entity.User;
import com.spring.service.OrderService;
import com.spring.service.RootUserService;
import com.spring.service.UserService;
import com.spring.util.String2UserPropertyEditor;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @Description
 * @Author danxiaodong
 * @Date 2023/7/24 14:24
 **/
public class Test {
    public static void main(String[] args) throws IOException {
        metadataReaderTest();
    }

    /**
     * 元数据读取器
     * 在Spring中需要去解析类的信息，比如类名、类中的方法、类上的注解，这些都可以称之为类的元数据，
     * 所以Spring中对类的元数据做了抽象，并提供了一些工具类。
     *
     * MetadataReader表示类的元数据读取器，默认实现类为SimpleMetadataReader。
     */
    public static void metadataReaderTest() throws IOException {
        SimpleMetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory();
        //构造一个MetadataReader读取文件
        MetadataReader metadataReader = readerFactory.getMetadataReader("com.spring." +
                "service.CustomBeanPostProcessor");
        //得到一个ClassMetadata，获取类名
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        System.out.println(classMetadata.getClassName());
        for (String interfaceName : classMetadata.getInterfaceNames()) {
            System.out.println(interfaceName);
        }

        //得到一个AnnotationMetadata，并获取类上的注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        for (String annotationType : annotationMetadata.getAnnotationTypes()) {
            System.out.println(annotationType);
        }
    }

    /**
     * beanPostProcessorTest
     */
    public static void beanPostProcessorTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        System.out.println(context.getBean("customFactoryBean"));
    }

    public static void typeConverterTest(){
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        typeConverter.registerCustomEditor(User.class, new String2UserPropertyEditor());
        //typeConverter.setConversionService(conversionService);
        User user = typeConverter.convertIfNecessary("tom,3", User.class);
        System.out.println(user.getUsername()+"--"+user.getAge());
    }


    /**
     * 类型转化器，将字符串转为User对象
     */
    public static void conditionalGenericConverterTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        RootUserService rootUserService = context.getBean("rootUserService", RootUserService.class);
        rootUserService.test();
    }

    /**
     * 类型转化器，将字符串转为User对象
     */
    public static void propertyEditorTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        RootUserService rootUserService = context.getBean("rootUserService", RootUserService.class);
        rootUserService.test();
    }

    /**
     * 事件发布
     */
    public static void applicationListenerTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        //发布事件
        context.publishEvent("kkk");
    }

    /**
     * 获取运行时环境
     */
    public static void getEnvironmentTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("====================");
        Map<String, Object> systemEnvironment = context.getEnvironment().getSystemEnvironment();
        System.out.println(systemEnvironment);

        System.out.println("====================");
        Map<String, Object> systemProperties = context.getEnvironment().getSystemProperties();
        System.out.println(systemProperties);

        System.out.println("====================");
        //功能最强大，可查询出所有的环境配置
        MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
        System.out.println(propertySources);

        System.out.println("====================");
        System.out.println(context.getEnvironment().getProperty("NO_PROXY"));
        System.out.println(context.getEnvironment().getProperty("sun.jnu.encoding"));
//        System.out.println(context.getEnvironment().getProperty("zhouyu"));
    }

    /**
     * ApplicationContext还拥有资源加载的功能，比如，可以直接利用ApplicationContext获取某个文件的内容
     * 你可以想想，如果你不使用ApplicationContext，而是自己来实现这个功能，就比较费时间了。
     */
    public static void getResourceTest() throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Resource resource = context.getResource("file://Users/danxiaodong/IdeaProjects/SpringCloudAlibaba/spring/pom.xml");
        System.out.println(resource.getFilename());

        Resource resource1 = context.getResource("https://www.baidu.com");
        System.out.println(resource1.contentLength());
        System.out.println(resource1.getURL());

        Resource resource2 = context.getResource("classpath:spring.xml");
        System.out.println(resource2.contentLength());
        System.out.println(resource2.getURL());

        //还可以一次获取多个
        Resource[] resources = context.getResources("classpath:com/spring/*.class");
        for (Resource re : resources) {
            System.out.println(re.contentLength());
            System.out.println(re.getFilename());
        }
    }

    /**
     * ApplicationContext 国际化
     * 有了这个Bean，你可以在你任意想要进行国际化的地方使用该MessageSource。
     * ApplicationContext两个比较重要的实现类：
     *      AnnotationConfigApplicationContext
     *      ClassPathXmlApplicationContext
     */
    public static void MessageSourceTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        //注意⚠️：配置文件必须是 messages开头，否则会报错
        String message_en = context.getMessage("test", null, new Locale("en"));
        String message_cn = context.getMessage("test", null, new Locale(""));

        System.out.println(message_en);
        System.out.println(message_cn);
    }

    //BeanDefinitionReader

    /**
     * ClassPathBeanDefinitionScanner
     * ClassPathBeanDefinitionScanner是扫描器，但是它的作用和BeanDefinitionReader类似，它可以进行扫描，扫描某个包路径，对扫描到的类
     * 进行解析，比如，扫描到的类上如果存在@Component注解，那么就会把这个类解析为一个BeanDefinition
     */
    public static void classPathBeanDefinitionScannerTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.refresh();

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context);
        scanner.scan("com.spring");

        System.out.println(context.getBean("adminService"));
    }

    /**
     * XmlBeanDefinitionReader
     * 可以解析<bean/>标签
     */
    public static void xmlBeanDefinitionReaderTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(context);
        int i = beanDefinitionReader.loadBeanDefinitions("spring.xml");
        System.out.println(i);
        System.out.println(context.getBean("stockService"));
    }

    /**
     * AnnotatedBeanDefinitionReader
     * 可以直接把某个类转换为BeanDefinition，并且会解析该类上的注解
     * 它能解析的注解是：@Conditional，@Scope、@Lazy、@Primary、@DependsOn、@Role、@Description
     */
    public static void annotatedBeanDefinitionReaderTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        AnnotatedBeanDefinitionReader beanDefinitionReader = new AnnotatedBeanDefinitionReader(context);
        //直接将OrderService转为BeanDefinition，解析该类上的注解，生成BeanDefinition
        beanDefinitionReader.register(OrderService.class);
        System.out.println(context.getBean("orderService"));
    }

    /**
     * BeanDefinition表示Bean定义，BeanDefinition中存在很多属性用来描述一个Bean的特点。
     * 比如：
     *  class，表示Bean类型
     *  scope，表示Bean作用域，单例或原型等
     *  lazyInit：表示Bean是否是懒加载
     *  initMethodName：表示Bean初始化时要执行的方法
     *  destroyMethodName：表示Bean销毁时要执行的方法
     *  还有很多...
     */
    public static void beanDefinitionTest(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        //生成一个BeanDefinition对象，并设置BeanClass，然后注册到ApplicationContext中
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
        //单例bean
        beanDefinition.setScope("singleton");
        //是否懒加载
        beanDefinition.setLazyInit(false);
        //bean类型
        beanDefinition.setBeanClass(UserService.class);
        //设置初始化方法（初始化方法必须要在bean中，且方法名与设置的一致）
        beanDefinition.setInitMethodName("init");
        //注册到ApplicationContext中
        context.registerBeanDefinition("userService", beanDefinition);

        System.out.println(context.getBean("userService"));
    }
}
