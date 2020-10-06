package com.zdc.org.util;

import com.zdc.org.mapper.Function;
import com.zdc.org.mapper.MapperBean;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlReadConfig {

    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    public MapperBean readMapper(String path){
        MapperBean mapperBean = new MapperBean();
        try {
            InputStream stream = loader.getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            Element root = document.getRootElement();
            mapperBean.setInterName(root.attributeValue("nameSpace").trim());
            List<Function> list = new ArrayList<Function>();
            for (Iterator rootIter = root.elementIterator(); rootIter.hasNext();){
                Function fun = new Function();    //用来存储一条方法的信息
                Element e = (Element) rootIter.next();
                String sqltype = e.getName().trim();
                String funcName = e.attributeValue("id").trim();
                String sql = e.getText().trim();
                String resultType = e.attributeValue("resultType").trim();
                fun.setSqltype(sqltype);
                fun.setFuncName(funcName);
                Object newInstance= Class.forName(resultType).newInstance();
                fun.setResultType(newInstance);
                fun.setSql(sql);
                list.add(fun);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mapperBean;
    }

}
