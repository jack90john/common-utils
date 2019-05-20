package com.jack90john.common.utils.bean.trans;

import java.lang.annotation.*;

/**
 * Description: 转换对象标志
 * Designer: jack
 * Date: 2019-05-16
 * Version: 1.1.1.RELEASE
 */

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BeanTrans {

    String[] ignoreFields() default {};  //需要忽略的变量名

}
