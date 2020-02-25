package com.pdt.core.mybatis.utils;

import com.pdt.core.mybatis.bean.Function;
import com.pdt.core.utils.BeanFill;
import com.pdt.core.utils.JDBC;
import com.pdt.ssm.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UseJDBC {

    public static Object SelectOne(Function function,Object[] arg) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object obj = null;
        Connection conn = JDBC.getConn();
        String sql = function.getSql();
        PreparedStatement pst=conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if(rs.first()){
            obj = BeanFill.handleSQL(function.getResultType(),rs);
        }
        JDBC.close(conn, pst);
        return obj;
    }

    public static List SelectList(Function function,Object[] arg) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        List list = new ArrayList();
        Connection conn = JDBC.getConn();
        String sql = function.getSql();
        PreparedStatement pst=conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            if(function.getSetGenericReturnType() == null){
                list.add(null);
            }else{
                list.add(BeanFill.handleSQL(function.getSetGenericReturnType(),rs));
            }
        }
        JDBC.close(conn, pst);
        System.out.println(list);
        return list;
    }

}
