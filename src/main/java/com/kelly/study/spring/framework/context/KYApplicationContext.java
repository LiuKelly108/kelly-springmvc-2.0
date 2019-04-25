package com.kelly.study.spring.framework.context;

import com.kelly.study.spring.framework.annotation.KYAutowired;
import com.kelly.study.spring.framework.annotation.KYController;
import com.kelly.study.spring.framework.annotation.KYService;
import com.kelly.study.spring.framework.beans.KYBeanWrapper;
import com.kelly.study.spring.framework.beans.config.KYBeanDefinition;
import com.kelly.study.spring.framework.beans.config.KYBeanPostProcessor;
import com.kelly.study.spring.framework.beans.support.BeansException;
import com.kelly.study.spring.framework.beans.support.KYBeanDefinitionReader;
import com.kelly.study.spring.framework.context.support.KYDefaultListableBeanFactory;
import com.kelly.study.spring.framework.core.KYBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KYApplicationContext extends KYDefaultListableBeanFactory implements KYBeanFactory {

    private String[] configLocations ;
    private KYBeanDefinitionReader reader ;

    /*
     *单例IOC容器缓存，保证注册是单例的容器
     */
    private Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();

    /*
     * 用来存储所有被代理的对象
     */
    private Map<String,KYBeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, KYBeanWrapper>();


    //构造方法
    public KYApplicationContext(String...configLocations){
        this.configLocations = configLocations ;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * IOC容器初始化过程
     * @throws Exception
     */
    public void refresh() throws Exception{
        //1.定位：定位配置文件
        reader = new KYBeanDefinitionReader(this.configLocations);

        //2.加载：加载配置文件，扫描相关的类，把它们封装成BeanDefinition
        List<KYBeanDefinition> beanDefinitionList = reader.loadBeanDefinitions();

        //3.注册：把配置信息放置到容器中
        doRegisterBeanDefinition(beanDefinitionList);

        //4.把不是延时加载的类，提前初始化
        doAutoWired();

    }

    private void doAutoWired() {
    }


    private void doRegisterBeanDefinition(List<KYBeanDefinition> beanDefinitionList) {
    }

    /**
     * 依赖注入： 1、获取BeanDefinition中的信息 2、通过反射机制创建一个实例并返回
     * Spring做法：用一个BeanWrapper进行一次包装，
     *           目的是 保留原来的oop关系以及需要对它进行扩展，增强（为AOP打基础）
     * @param name   bean的名字
     * @return
     * @throws BeansException
     */
    public Object getBean(String name) throws BeansException {
        //获得父类的beanDefinition
        KYBeanDefinition beanDefinition = this.beanDefinitionMap.get(name);
        try{
         //生成通知事件
            KYBeanPostProcessor beanPostProcessor = new KYBeanPostProcessor();
            ////使用默认的无参构造方法实例化Bean对象
            Object instance = instantiateBean(beanDefinition);
            if(null == instance){
                return  null ;
            }

            //在实例初化以前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, name);
            KYBeanWrapper beanWrapper = new KYBeanWrapper(instance);
            this.beanWrapperMap.put(name,beanWrapper);

            //实力初始化后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance,name);
            //注入
            populateBean(name,instance);

            return this.beanWrapperMap.get(name).getWrappedInstance();

        }catch (Exception e){
            e.printStackTrace();
            return null ;
        }

    }

    /**
     * 注入
     * @param name
     * @param instance
     */
    private void populateBean(String name, Object instance) {
        Class<?> clazz = instance.getClass();
        if(!(clazz.isAnnotationPresent(KYController.class)
                || clazz.isAnnotationPresent(KYService.class))){
            return;
        }
        //取到实例的成员变量
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields){
            if(!field.isAnnotationPresent(KYAutowired.class)){
                continue;
            }
            KYAutowired annotation = field.getAnnotation(KYAutowired.class);
            String autoWiredBeanName = annotation.value().trim();
            if("".equals(autoWiredBeanName)){
                autoWiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try{
                //反射注入值
                field.set(instance,this.beanWrapperMap.get(autoWiredBeanName).getWrappedInstance());

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 传入一个beanDefinition,返回一个实例bean
     * @param beanDefinition
     * @return
     */
    private Object instantiateBean(KYBeanDefinition beanDefinition) {
        Object instance = null ;
        String className = beanDefinition.getBeanClassName();
        try {

            //假设默认的类是单例
            if(this.factoryBeanObjectCache.containsKey(className)){
                instance = factoryBeanObjectCache.get(className);
            }else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);
            }
            return instance ;

        }catch (Exception e){
            e.printStackTrace();

        }
        return null ;
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return null;
    }




}
