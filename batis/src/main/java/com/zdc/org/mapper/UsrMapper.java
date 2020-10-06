package com.zdc.org.mapper;

import com.zdc.org.annotion.MyInsert;
import com.zdc.org.annotion.MyParam;


public interface UsrMapper {
    @MyInsert("insert into user(name,age) values(#{name},#{age})")
    public int insertUser(@MyParam("name") String name, @MyParam("age")Integer age);
}
