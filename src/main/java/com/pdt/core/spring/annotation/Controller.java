package com.pdt.core.spring.annotation;

import java.lang.annotation.*;

/**
 * @Author: nanJunYu
 * @Description: 标示为控制层
 * @Date: Create in  2018/8/13 11:06
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
}
