package com.pdt.core.mybatis.utils;

import com.pdt.core.mybatis.bean.Function;
import com.pdt.core.mybatis.bean.MapperBean;
import com.pdt.ssm.bean.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapperProxy<T> implements InvocationHandler{
	private MapperBean mapperBean;

	public MapperProxy(MapperBean mapperBean) {
		this.mapperBean = mapperBean;
	}

	@Override
	public T invoke(Object proxy, Method method, Object[] args) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		List<Function> list = mapperBean.getList();
		if(null != list || 0 != list.size()){
			for(Function function : list) {
				// 看id是否和接口的方法名一样
 				// 应该判断参数数量和格式是不是对应的
				if(method.getName().equals(function.getFuncName())){
					String ann = function.getSqltype();
					if(ann.equals("select")){
						if(function.getResultType().equals("java.util.List")){
							return (T) UseJDBC.SelectList(function,args);
						}else{
							return (T) UseJDBC.SelectOne(function,args);
						}
					}
				}
			}
		}
		return null;
	}
}