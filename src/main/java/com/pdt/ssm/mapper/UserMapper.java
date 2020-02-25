package com.pdt.ssm.mapper;


import com.pdt.core.mybatis.annotation.Mapper;
import com.pdt.core.mybatis.annotation.Select;
import com.pdt.ssm.bean.User;

import java.util.List;


/**
 * @作者：刘时明
 * @时间:2018/12/27-14:32
 * @说明：
 */
@Mapper("UserMapper")
public interface UserMapper {
    @Select("select * from t_test")
    List<User> test(String id);

    @Select("select * from t_test")
    User test4(String id);
}
