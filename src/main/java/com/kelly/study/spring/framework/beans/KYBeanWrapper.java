package com.kelly.study.spring.framework.beans;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class KYBeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass ;

    public KYBeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance ;
        this.wrappedClass = wrappedInstance.getClass();
    }

}
