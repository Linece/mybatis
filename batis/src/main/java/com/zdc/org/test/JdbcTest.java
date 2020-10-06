package com.zdc.org.test;

import com.zdc.org.mapper.UsrMapper;
import com.zdc.org.aop.SqlSession;

public class JdbcTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        //使用动态代理技术虚拟调用方法
        UsrMapper userMapper = SqlSession.getMapper(UsrMapper.class);
        int result = userMapper.insertUser("Larry", 40);
        System.out.println("insert result:" + result);
    }
}
