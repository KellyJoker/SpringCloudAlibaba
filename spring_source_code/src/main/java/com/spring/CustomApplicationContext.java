package com.spring;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 自定义
 * @Author danxiaodong
 * @Date 2023/7/23 13:06
 **/
public class CustomApplicationContext {
    /**
     * spring启动类
     */
    private Class configClass;
    /**
     * bean定义
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    /**
     * 单例池，用于存放生成的单例对象
     */
    private Map<String, Object> singletonObjects = new HashMap<>();
    /**
     * beanPostProcessorList
     */
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public CustomApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描
        scan(configClass);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            //创建bean
            if (beanDefinition.getScope().equals("singleton")) {
                //单例bean需要在初始化时创建并塞入单例池
                singletonObjects.put(beanName, createBean(beanName, beanDefinition));
            }
        }
    }

    /**
     * 根据bean的定义创建bean
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        Object instance = null;
        try {
            //实例化--可以在这里推断构造方法
            instance = clazz.getConstructor().newInstance();
            //依赖注入
            //获取该类的属性
            for (Field field : clazz.getDeclaredFields()) {
                //如果属性上有@Autowired注解
                if (field.isAnnotationPresent(Autowired.class)) {
                    /**
                     * 将此对象的 accessible 标志设置为指示的布尔值。值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。
                     * 值为 false 则指示反射的对象应该实施 Java 语言访问检查;实际上setAccessible是启用和禁用访问安全检查的开关,
                     * 并不是为true就能访问为false就不能访问 ；
                     * 由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的
                     */
                    field.setAccessible(true);
                    //如何将属性赋值？可以再一次调用getBean()方法获取
                    field.set(instance, getBean(field.getName()));
                }
            }
            //aware回调接口
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            //初始化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            //初始化，若实现了InitializingBean接口，则调用afterPropertiesSet()方法
            if (instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }

            //初始化后
            //如何让spring知道该执行用户自定义的实现BeanPostProcessor接口的类呢？最好的方法是添加@Component等注解
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 获取bean
     * 思考一个问题：我们在使用 getBean() 方法获取bean的时候，需要传入 beanName 获取 bean 实例，则需要重新扫描包路径下的 class 文件，
     *            根据beanName找到对应的 class 并加载获取 bean 的定义，创建 bean，然后返回。然而我们在初始化 context 的时候，已经加
     *            载了一遍包路径下的所有文件，获取到了 bean 的定义，此时使用 getBean() 方法再次获取 bean 的定义的时候，会显得多次一举。
     * Spring解决方式：因此 Spring 会创建一个 map，将 beanName 作为 key，bean 的定义作为 value，初始化的时候将值塞入 map 中。当外部
     *               调用 getBean() 方法时，直接根据 beanName 从 map 中获取 bean 的定义，创建 bean 实例，无须重复加载。 Spring 同
     *               时创建了一个单例池，在初始化时创建单例 bean，并将其塞入单例池，之后每次获取都从单例池获取，以保证每次获取的都是同一
     *               个 bean。
     * @param beanName
     * @return bean对象
     */
    public Object getBean(String beanName) {
        //若未找到该bean定义，则不存在
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException();
        }
        //若找到，则根据bean定义创建bean
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if ("singleton".equals(beanDefinition.getScope())) {
            //单例，从单例池获取
            Object singletonBean = singletonObjects.get(beanName);
            if (singletonBean == null) {
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        } else {
            //原型，每一次调用都创建一个bean
            Object prototypeBean = createBean(beanName, beanDefinition);
            return prototypeBean;
        }
    }

    /**
     * 初始化扫描
     * @param configClass
     */
    private void scan(Class configClass) {
        // 扫描包路径
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            // 1.获取注解
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            // 2.获取扫描路径（相对路径 ）
            String path = componentScanAnnotation.value().replace(".", "/");
            /**
             * 此时需要获取的是target文件下的路径，即编译后的class文件路径。
             * 类加载器管理着target文件夹下的文件
             */

            // 3.利用类加载器获取所需的class文件路径
            ClassLoader classLoader = CustomApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            // 4.拿到路径后，遍历路径下的class文件
            File file = new File(resource.getFile());
            //如果是文件夹，遍历
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    String absolutePath = f.getAbsolutePath();
                    //找到class文件后，还需要判断class中有没有@Component注解。那该怎么判断呢？
                    //可以通过文件直接获取注解么？可以，spring源码里使用的是ASM技术。
                    //还有最简单的方式：将class通过类加载器加载成class对象，然后获取注解

                    //需要将相对路径转为包路径，"com.xiaodong.service.UserService"
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"))
                            .replace("/", ".");
                    try {
                        /**
                         * 一般我们需要获取一个类的注解等信息，需要将类加载到JVM中来获取，这样非常不方便，而且消耗性能。
                         * ASM技术就是直接读取字节码文件，解析字节码文件后拿到相对应的类信息（因为字节码文件是有固定格式的，可以根据特定
                         * 的方式读取字节码文件中的内容），这样更加快捷方便，因为我们只需要解析class文件即可，无须加载class文件到JVM中。
                         * 在Spring中需要去解析类的信息，比如类名、类中的方法、类上的注解，这些都可以称之为类的元数据，所以Spring中对类
                         * 的元数据做了抽象，并提供了一些工具类（MetadataReader、ClassMetadata、AnnotationMetadata）。
                         * MetadataReader表示类的元数据读取器，默认实现类为SimpleMetadataReader。SimpleMetadataReader去解析类
                         * 时，使用的ASM技术。
                         * 为什么要使用ASM技术？Spring启动的时候需要去扫描，如果指定的包路径比较宽泛，那么扫描的类是非常多的，那如果在
                         * Spring启动时就把这些类全部加载进JVM了，这样不太好，所以使用了ASM技术。
                         */
                        Class<?> clazz = classLoader.loadClass(absolutePath);
                        //判断该bean是否有@Component等注解
                        if (clazz.isAnnotationPresent(Component.class)) {
                            //若有@Component等注解，则注册成为spring容器中的bean；若没有，则无须管理

                            //isAssignableFrom() 是用来判断子类和父类的关系的，或接口的实现类和接口的关系的
                            //clazz是一个类，不能用instanceof来判断，instanceof只能用于判断对象实例
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                //实例化
                                BeanPostProcessor instance = (BeanPostProcessor) clazz.getConstructor().newInstance();
                                //添加到list中
                                beanPostProcessorList.add(instance);
                            }
                            //获取beanName
                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();
                            if ("".equals(beanName)) {
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }
                            //bean定义
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz);
                            //若有@Component等注解，再判断该bean是否被@Scope注解
                            if (clazz.isAnnotationPresent(Scope.class)) {
                                //若有@Scope注解，获取注解的value值，若是singleton，则是单例bean
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                String value = scopeAnnotation.value();
                                //bean定义
                                beanDefinition.setScope(value);
                            } else {
                                //若没有@Scope注解，也是单例bean
                                beanDefinition.setScope("singleton");
                            }
                            //将bean定义塞入map
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException | NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
