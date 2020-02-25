package com.pdt.core.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Author: nanJunYu
 * @Description:访问记录映射 作用范围内和方法
 * @Date: Create in  2018/8/13 11:08
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapper {
    String value() default "";
}
