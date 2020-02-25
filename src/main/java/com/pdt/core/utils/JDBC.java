package com.pdt.core.utils;

import java.sql.*;

public class JDBC {

        //获取连接对象的方法(静态的）
        public static Connection getConn(){
            Connection conn=null;
            try {
                //1.注册驱动(静态方法)(包名+类名）
                Class.forName("com.mysql.cj.jdbc.Driver");
                //2.获取连接对象(导包都导sql里面的，不导jdbc里的；多态！报异常是因为用户输入的串可能写错）后面设置下数据格式
                String url="jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
                String user="root";
                String password="123456";
                conn= DriverManager.getConnection(url,user,password);
            } catch (ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return conn;
        }
        //释放资源
        public static void close(Connection conn, Statement sta){
            if(sta!=null){
                try {
                    sta.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        //释放资源2
        public static void close(Connection conn, Statement sta, ResultSet rs){
            if(rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(sta!=null){
                try {
                    sta.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
}
