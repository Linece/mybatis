package com.zdc.org.mapper;

import java.util.List;

public class MapperBean {
    //接口名
    private String interName;

    private List<Function> list;

    public String getInterName() {
        return interName;
    }

    public void setInterName(String interName) {
        this.interName = interName;
    }

    public List<Function> getList() {
        return list;
    }

    public void setList(List<Function> list) {
        this.list = list;
    }
}

