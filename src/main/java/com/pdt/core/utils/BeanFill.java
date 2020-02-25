package com.pdt.core.utils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanFill {
    public static Object handleSQL(String typeStr,ResultSet rs) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Object res = Class.forName(typeStr).newInstance();
        Field[] fields = Class.forName(typeStr).getDeclaredFields();
        for (Field field : fields) {
            String fieldType = field.getType().getName();
            String fieldName = field.getName();
            //打开私有属性的权限修改
            field.setAccessible(true);
            if(fieldType.equals("java.lang.String")){
                field.set(res,rs.getString(fieldName));
            }
        }
        return res;
    }

    public static Object handleRequest(String typeStr, HttpServletRequest req) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object res = Class.forName(typeStr).newInstance();
        Field[] fields = Class.forName(typeStr).getDeclaredFields();
        for (Field field : fields) {
            String fieldType = field.getType().getName();
            String fieldName = field.getName();
            //打开私有属性的权限修改
            field.setAccessible(true);
            field.set(res,req.getParameter(fieldName));
        }
        return res;
    }
}
