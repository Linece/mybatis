package com.zdc.org.aop;

import java.lang.reflect.Proxy;

public class SqlSession {

    //加载Mapper接口
    @SuppressWarnings("unchecked")
    public static <T> T getMapper(Class<T> classz)
            throws IllegalArgumentException, InstantiationException, IllegalAccessException {
        return (T) Proxy.newProxyInstance(classz.getClassLoader(),
                new Class[]{classz}, new MyInvocationHandlerMybatis(classz));
    }
}
