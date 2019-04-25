package com.kelly.study.spring.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})  //类的注解方式
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KYService {
    String value() default "";
}
