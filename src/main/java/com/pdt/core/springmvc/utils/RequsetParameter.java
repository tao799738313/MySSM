package com.pdt.core.springmvc.utils;


import com.pdt.core.mybatis.utils.UseJDBC;
import com.pdt.core.springmvc.annotation.RequstParam;
import com.pdt.core.utils.BeanFill;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class RequsetParameter {
    public static List handle(HttpServletRequest req, HttpServletResponse resp, Parameter[] parameters) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
         List list = new ArrayList();
         for (Parameter parameter : parameters) {
             if (parameter.getAnnotation(RequstParam.class) != null){
                 RequstParam RequstParam = parameter.getAnnotation(RequstParam.class);
                 String RequstParamValue = RequstParam.value();
                 String parameterType = parameter.getType().getSimpleName();
                 String str = req.getParameter(RequstParamValue);
                 if (parameterType.equals("String")) {
                     list.add(str);
                 } else if (parameterType.equals("Integer") || parameterType.equals("int")) {
                     list.add(Integer.valueOf(str));
                 }
             }else{
                 String parameterType = parameter.getType().getName();
                 if(parameterType.equals("javax.servlet.http.HttpServletRequest")) {
                     list.add(req);
                 }else if (parameterType.equals("javax.servlet.http.HttpServletResponse")) {
                     list.add(resp);
                 }else{
                     list.add(BeanFill.handleRequest(parameterType,req));
                     // 主动注入Bean
                 }
             }
         }
         return list;
    }
}
