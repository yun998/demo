package com.yun.spring;

import com.yun.spring.annotations.Autowired;
import com.yun.spring.annotations.Component;
import com.yun.spring.annotations.ComponentScan;
import com.yun.spring.annotations.Scope;
import com.yun.spring.bean.BeanDefinition;
import com.yun.spring.interfaces.BeanNameAware;
import com.yun.spring.interfaces.BeanPostProcessor;
import com.yun.spring.interfaces.InitializingBean;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YunApplicationContext {
    // 配置类
    private Class configClass;
    // 单例池
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    // 对bean的描述
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    // 后置处理器
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();


    public YunApplicationContext(Class configClass){
        this.configClass = configClass;
        // 解析配置类
        scan(configClass);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            // 如果是单例，则放入单例池
            if ("singleton".equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    /**
     * 创建bean
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            // 依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Object bean = getBean(declaredField.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(instance, bean);
                }
            }
            // Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware)instance).setBeanName(beanName);
            }
            // 初始化前对bean的修改
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessorBeforeInitializing(instance, beanName);
            }
            // 初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }
            // 初始化后对bean的修改
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessorAfterInitializing(instance, beanName);
            }
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 扫描
     * @param configClass
     */
    private void scan(Class configClass) {
        // 解析配置类
        // 获取ComponentScan注解
        ComponentScan classDeclaredAnnotations = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        if(classDeclaredAnnotations == null) {throw new RuntimeException("not @ComponentScan");}
        // 获取扫描路径
        String path = classDeclaredAnnotations.value().replace(".", "/");
        // System.out.println(path);
        ClassLoader classLoader = YunApplicationContext.class.getClassLoader();
        // file:/D:/idea/JavaProject/demo/target/classes/com/yun/spring/service
        URL resource = classLoader.getResource(path);
        // 获取地址长度
        int length = classLoader.getResource("").toString().length() - 6;
        File file = new File(resource.getFile());
        // 判断是否为文件目录
        if(file.isDirectory()){
            File[] files = file.listFiles();
            recursiveFile(files, length, classLoader);
        }
    }

    /**
     * 取出所有文件
     * @param files
     * @param length
     */
    private void recursiveFile(File[] files, int length, ClassLoader classLoader){
        for (File f : files) {
            // 如果是目录则递归
            if(f.isDirectory()){
                File[] fs = f.listFiles();
                recursiveFile(fs, length, classLoader);
            }

            String fileName = f.getAbsolutePath();
            // 处理后缀为“.class”的文件
            if(fileName.endsWith(".class")){

                String className = fileName.substring(length, fileName.indexOf(".class")).replace("\\", ".");

                try {
                    Class clazz = classLoader.loadClass(className);
                    // 判断当前文件是否有@Component
                    if (clazz.isAnnotationPresent(Component.class)) {

                        // 把bean存入beanPostProcessorList，在初始化前后使用
                        if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                            try {
                                BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(instance);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                        }

                        // 取出@Component的内容
                        Component componentAnnotation = (Component)clazz.getDeclaredAnnotation(Component.class);
                        String beanName = componentAnnotation.value();

                        if ("".equals(beanName)){
                            beanName = className.substring(className.lastIndexOf(".") + 1);
                            char[] chars = beanName.toCharArray();
                            if (chars[0] >= 'A' && chars[0] <= 'Z'){
                                chars[0] += 32;
                                beanName = String.valueOf(chars);
                            }
                        }

                        if(beanDefinitionMap.containsKey(beanName)){throw new RuntimeException("BeanName重复");}

                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setClazz(clazz);
                        // 判断类是否存在注解，没有则是单例
                        if(clazz.isAnnotationPresent(Scope.class)) {
                            // 把@Scope的内容存入BeanDefinition
                            Scope scopeAnnotation = (Scope)clazz.getDeclaredAnnotation(Scope.class);
                            beanDefinition.setScope(scopeAnnotation.value());
                        } else {
                            beanDefinition.setScope("singleton");
                        }
                        beanDefinitionMap.put(beanName, beanDefinition);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public Object getBean(String beanName){
        // 判断中beanDefinitionMap有没有这个bean
        if(beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            // 如果是单例，则把单例池中的bean返回
            if ("singleton".equals(beanDefinition.getScope())) {
                return singletonObjects.get(beanName);
            } else {
                // 返回一个新的bean
                return createBean(beanName, beanDefinition);
            }
        } else {
            throw new RuntimeException("not Bean");
        }
    }
}
