package com.pdt.ssm.service;


import com.pdt.core.spring.annotation.Autowired;
import com.pdt.core.spring.annotation.Service;
import com.pdt.ssm.bean.User;
import com.pdt.ssm.mapper.UserMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: nanJunYu
 * @Description:
 * @Date: Create in  2018/8/13 14:40
 */
@Service(value = "UserService")
public class UserService {
    @Autowired("UserMapper")
    UserMapper userMapper;

    public List test(String id, HttpServletRequest req) {
        List list = userMapper.test(id);
        return list;
    }

    public User test4(String id) {
        User user = userMapper.test4(id);
        return user;
    }
}
