package com.jack90john.common.utils.bean.trans;

import java.lang.annotation.*;

/**
 * Description:
 * Designer: jack
 * Date: 2019-05-16
 * Version: 1.0.0
 */

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BeanTrans {

    String[] ignoreFields() default{};

}
