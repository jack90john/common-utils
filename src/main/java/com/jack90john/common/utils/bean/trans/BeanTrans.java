package com.jack90john.common.utils.bean.trans;

import java.lang.annotation.*;

/**
 * 转换对象标志
 *
 * @author jack
 * @version 1.1.1
 * @since 1.0.0.RELEASE
 * @deprecated 为了使用更加简单，不再强制标注待转换类，如果需要忽略部分字段可以使用{@link IgnoreFields}注解。
 *
 * <p>这种写法
 *
 * <pre>
 * {@literal @}{@code BeanTrans(ignoreFields={"a","c"})
 * public class A{
 *     String a;
 *     String b;
 *     String c;
 *     }
 * }</pre>
 *
 * 可以替换为
 * <pre>
 * {@literal @}{@code IgnoreFields({"a","c"})
 * public class A{
 *     String a;
 *     String b;
 *     String c;
 *     }
 * }</pre>
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Deprecated(since = "2.1.0.RELEASE")
public @interface BeanTrans {

    String[] ignoreFields() default {};  //需要忽略的变量名

}



