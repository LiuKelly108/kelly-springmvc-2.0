package com.kelly.study.spring.framework.beans.support;


import com.sun.istack.internal.Nullable;

public class BeansException extends RuntimeException {

    private static final long serialVersionUID = -1105194001279877059L;


    public BeansException(String msg) {
        super(msg);
    }

    /**
     * 使用msg和 throwable 创建一个exception
     * @param msg
     * @param cause
     */
    public BeansException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }


}
