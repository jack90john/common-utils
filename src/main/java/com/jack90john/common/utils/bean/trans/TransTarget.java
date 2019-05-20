package com.jack90john.common.utils.bean.trans;

import java.lang.annotation.*;

/**
 * Description: 目标对象注解
 * Designer: jack
 * Date: 2019-05-16
 * Version: 1.1.1.RELEASE
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TransTarget {

    String value();   //目标对象变量名

}
