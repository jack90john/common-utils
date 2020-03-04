package com.jack90john.common.utils.bean.trans;

import java.lang.annotation.*;

/**
 * 当前字段在哪些类中不需要转换。
 *
 * @author jack
 * @version 1.0.0
 * @since 2.2.0.RELEASE
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IgnoreClass {
    Class<?>[] value() default {};
}
