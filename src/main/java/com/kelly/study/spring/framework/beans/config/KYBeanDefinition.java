package com.kelly.study.spring.framework.beans.config;

import lombok.Data;

/**
 * 用来存储配置文件中的信息
 * 相当于保存内存中的配置
 */
@Data
public class KYBeanDefinition {

    private  String beanClassName;  //beanClass的名称
    private boolean lazyInit=false; //初始化懒加载
    private String factoryBeanName;

}
