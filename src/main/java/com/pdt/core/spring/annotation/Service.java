package com.pdt.core.spring.annotation;

import java.lang.annotation.*;

/**
 * @Author: nanJunYu
 * @Description: 启动时将自动注册到容器
 * @Date: Create in  2018/8/13 14:33
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
