package com.zdc.org.aop;

import com.zdc.org.util.JDBCUtils;
import com.zdc.org.util.SQLUtils;
import com.zdc.org.annotion.MyInsert;
import com.zdc.org.annotion.MyParam;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MyInvocationHandlerMybatis implements InvocationHandler {

    private Object object;

    public MyInvocationHandlerMybatis(Object object){
        this.object = object;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("使用动态代理技术拦截接口方法开始");
        MyInsert insert = method.getDeclaredAnnotation(MyInsert.class);
        if (insert != null){
            return exeInsertSQL(insert, proxy, method, args);
        }

//        //二、查询思路
//        //1判断方法上是否存在@ExtSelect注解
//        ExtSelect extSelect = method.getDeclaredAnnotation(ExtSelect.class);
//        if(extSelect != null){
//            // 2、获取SQL语句，获取注解的Select语句
//            String selectSQL = extSelect.value();
//            //3、获取方法的参数和SQL参数进行匹配
//            ConcurrentHashMap<Object, Object> paramMap = paramsMap(proxy, method, args);
//            //4、替换参数变？ 传递方式
//            List<String> sqlSelectParams = SQLUtils.sqlSelectParameter(selectSQL);
//            //5.传递参数
//            List<Object> sqlParams = new ArrayList<>();
//            for(String parameterName : sqlSelectParams){
//                Object parameterValue = paramMap.get(parameterName);
//                sqlParams.add(parameterValue);
//            }
//            // 6.将sql语句换成？
//            String newSql = SQLUtils.parameQuestion(selectSQL, sqlSelectParams);
//
//            System.out.println("newSQL:" + newSql + ", sqlParams:" +sqlParams.toString() );
//            //6、调用jdbc底层代码执行语句；
//            ResultSet res = JDBCUtils.query(newSql, sqlParams);
//
//            //7、使用反射机制实例对象### 获取方法返回的类型，进行实例化
//            //思路：
//            //1).使用反射机制获取方法类型，
//            //2). 判断是否有结果集，在进行初始化
//            //3).使用反射机制给对象复制
//            while(!res.next()){
//                return null;
//                //System.out.println(query.getObject(1));
//            }
//
//            //下标往上移动一位
//            res.previous();
//
//            //1).使用反射机制获取方法类型，
//            Class<?> retrunType = method.getReturnType();
//            Object object = retrunType.newInstance();
//            while(res.next()){
//                //方式一
//                /*for(String parameterName: sqlSelectParams){
//                    Object resultValue = res.getObject(parameterName);
//                    //使用Java的反射赋值
//                    Field field = retrunType.getDeclaredField(parameterName);
//                    //私有方法允许访问
//                    field.setAccessible(true);
//                    field.set(object, resultValue);
//                }*/
//                //方式二 获取当前所有的属性
//                Field[] declaredFields = retrunType.getDeclaredFields();
//                for(Field field : declaredFields){
//                    String fildName = field.getName();
//                    Object fildVale = res.getObject(fildName);
//                    field.setAccessible(true);
//                    field.set(object, fildVale);
//                }
//            }
//            return object;
//
//
//
//        }
        return null;
    }

    private ConcurrentHashMap<Object, Object> paramsMap(Object proxy, Method method, Object[] args){
        ConcurrentHashMap<Object,Object> paramMap = new ConcurrentHashMap<>();
        //获取方法上的参数
        Parameter[] parameters = method.getParameters();

        for(int i = 0; i <parameters.length; i++){
            //参数名称
            Parameter parameter = parameters[i];
            MyParam extParam = parameter.getDeclaredAnnotation(MyParam.class);
            if(extParam != null){
                //参数名称
                String paramName = extParam.value();
                Object paramValue = args[i];
                //System.out.println("paramName:" + paramName  + " paramValue:" + paramValue);
                paramMap.put(paramName,  paramValue);
            }

        }
        return paramMap;
    }

    private List<Object> sqlInsertParameter(String[] sqlInsertParameter , ConcurrentHashMap<Object, Object> paramMap){
        List<Object> sqlParams = new ArrayList<>();

        for(String paramName: sqlInsertParameter){
            Object paramValue =   paramMap.get(paramName);
            sqlParams.add(paramValue);
        }
        return sqlParams;
    }

    public Object exeInsertSQL(MyInsert extInsert,Object proxy, Method method, Object[] args) {
        //方法上存在@ExtInsert，获取他的SQL语句
        //2、获取SQL语句，获取注解的Insert语句
        String insertSql = extInsert.value();
        //System.out.println("insertSql:" + insertSql);
        //3、获取方法的参数和SQL参数进行匹配
        //定义一个Map集合，KEY为@ExtParamValue， Value 结果为参数值
        ConcurrentHashMap<Object, Object> paramMap = paramsMap(proxy, method, args);

        //存放sql执行参数--参数绑定过程
        String[] sqlInsertParameter = SQLUtils.sqlInsertParameter(insertSql);
        List<Object> sqlParams = sqlInsertParameter(sqlInsertParameter, paramMap);

        //4、替换参数变？
        String newSQL = SQLUtils.parameQuestion(insertSql, sqlInsertParameter);
        System.out.println("newSQL:" + newSQL + ",sqlParams:" + sqlParams.toString());
        //5、调用jdbc底层代码执行语句；
        return JDBCUtils.insert(newSQL, false, sqlParams);
    }
}
