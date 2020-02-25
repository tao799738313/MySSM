package com.pdt.core.springmvc.utils;


import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReturnParameter {
    public static void handle(String returnTypeSimpleName, List parameList, Method method, Object controller, HttpServletResponse resp)throws Exception{
        if(returnTypeSimpleName.equals("String")){
            // 这里应该判断有没有body注解的，没有就返回页面，有返回字符串
            String res = (String) method.invoke(controller,parameList.toArray());
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().println(res);
        }else if(returnTypeSimpleName.equals("List")){
            List list = (List) method.invoke(controller,parameList.toArray());
            String res = JSON.toJSONString(list);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().println(res);
        }else if(returnTypeSimpleName.equals("Map")){
            Map map = (Map) method.invoke(controller,parameList.toArray());
            String res = JSON.toJSONString(map);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().println(res);
        }else{
            Object obj = method.invoke(controller,parameList.toArray());
            String res = JSON.toJSONString(obj);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().println(res);
        }
    }
}
