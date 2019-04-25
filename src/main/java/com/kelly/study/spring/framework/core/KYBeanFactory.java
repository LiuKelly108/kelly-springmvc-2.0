package com.kelly.study.spring.framework.core;

import com.kelly.study.spring.framework.beans.support.BeansException;

/**
 * 单例工厂的顶层设计
 */
public interface  KYBeanFactory {


    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     * @param name   bean的名字
     * @return
     * @throws BeansException
     */
    Object getBean(String name) throws BeansException;


    /**根据beanClass的类型获得bean
     * @param beanClass  bean的class类型
     * @return
     * @throws Exception
     */
    public Object getBean(Class<?> beanClass) throws  Exception ;

}
