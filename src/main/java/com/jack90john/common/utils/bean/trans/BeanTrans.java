package com.jack90john.common.utils.bean.trans;

import java.lang.annotation.*;

/**
 * @apiNote 转换对象标志
 * @author jack
 * @version 1.1.1
 * @since 1.0.0.RELEASE
 */

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BeanTrans {

    String[] ignoreFields() default {};  //需要忽略的变量名

}
