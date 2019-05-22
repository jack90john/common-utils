package com.jack90john.common.utils.bean.trans;

import java.lang.annotation.*;

/**
 * 不需要转换的变量名
 *
 * @author jack
 * @version 1.0.0
 * @since 2.1.0.RELEASE
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IgnoreFields {
    String[] value() default {};
}
