package com.kelly.study.spring.framework.context.support;

import com.kelly.study.spring.framework.beans.config.KYBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class KYDefaultListableBeanFactory extends  KYAbstractApplicationContext{

    /*
     * 储存注册信息的beanDefinition
     */
    protected final Map<String, KYBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, KYBeanDefinition>();

}
