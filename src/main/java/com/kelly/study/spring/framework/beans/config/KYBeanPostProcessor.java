package com.kelly.study.spring.framework.beans.config;

import com.kelly.study.spring.framework.beans.support.BeansException;
import com.sun.istack.internal.Nullable;

public class KYBeanPostProcessor {

    /**
     * 为bean的初始化提供回调入口
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Nullable
     public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 为bean的初始化提供回调入口
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
