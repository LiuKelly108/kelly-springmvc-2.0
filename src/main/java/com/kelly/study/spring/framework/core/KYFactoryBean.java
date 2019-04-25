package com.kelly.study.spring.framework.core;

import com.sun.istack.internal.Nullable;

public interface KYFactoryBean<T> {

    //获取容器管理的对象实例
    @Nullable
    T getBean() throws Exception;


}
