package com.yun.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
   RetentionPolicy.SOURCE：注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；
   RetentionPolicy.CLASS：注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期；
   RetentionPolicy.RUNTIME：注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
 */
@Retention(RetentionPolicy.RUNTIME)
/*
   ElementType.TYPE：接口、类、枚举
　　ElementType.FIELD：字段、枚举的常量
　　ElementType.METHOD：方法
　　ElementType.PARAMETER：方法参数
　　ElementType.CONSTRUCTOR：构造函数
　　ElementType.LOCAL_VARIABLE：局部变量
　　ElementType.ANNOTATION_TYPE：注解
　　ElementType.PACKAGE：包
 */
@Target(ElementType.TYPE)
// @Document：说明该注解将被包含在javadoc中
// @Inherited：说明子类可以继承父类中的该注解
public @interface Component {
    String value() default "";
}
