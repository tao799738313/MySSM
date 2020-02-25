package com.pdt.core.spring.annotation;

import java.lang.annotation.*;

/**
 * @Author: nanJunYu
 * @Description:自动装配
 * @Date: Create in  2018/8/13 11:08
 */
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
