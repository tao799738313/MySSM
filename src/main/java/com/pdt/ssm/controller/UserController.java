package com.pdt.ssm.controller;

import com.pdt.core.spring.annotation.Autowired;
import com.pdt.core.spring.annotation.Controller;
import com.pdt.core.springmvc.annotation.RequestMapping;
import com.pdt.core.utils.JDBC;
import com.pdt.ssm.bean.User;
import com.pdt.ssm.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: nanJunYu
 * @Description: ssm Controller 控制层
 * @Date: Create in  2018/8/13 14:38
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired("UserService")
    UserService userService;

    @RequestMapping(value = "/test")
    public List test(HttpServletRequest req, HttpServletResponse response,User user) {
        System.out.println(user);
        List list = userService.test("aaa123",req);
        return list;
    }

    @RequestMapping(value = "/test2")
    public String test2() throws SQLException {
        return "123";
    }

    @RequestMapping(value = "/test3")
    public Map test3() {
        Map map = new HashMap();
        map.put("name","name");
        map.put("age",18);
        return map;
    }

    @RequestMapping(value = "/test4")
    public User test4() {
        User user = userService.test4("aaa133");
        return user;
    }
}
