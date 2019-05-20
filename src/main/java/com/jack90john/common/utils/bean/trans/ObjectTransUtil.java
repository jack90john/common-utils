package com.jack90john.common.utils.bean.trans;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  对象转换工具
 * @author jack
 * @version 1.1.1
 * @since 1.0.0.RELEASE
 */

public class ObjectTransUtil {

    private ObjectTransUtil() {
    }

    /**
     * 转换对象方法，待转换对象需要标注{@link BeanTrans}注解
     * 待转换对象和转换后对象变量名称如果一样可以默认可以匹配
     * 如果待转换对象和转换后对象变量名不同，则需要通过{@link TransTarget}注解标注
     *
     * @param k      待转换对象，需要标注{@link BeanTrans}注解
     * @param vClass 结果对象class
     * @return 转换后得到待对象
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @see BeanTrans
     * @see TransTarget
     * @since 1.0.0.RELEASE
     */
    public static <K, V> V transBean(K k, Class<V> vClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<?> kClass = k.getClass();
        V v = vClass.getDeclaredConstructor().newInstance();
        // 检查是否标注BeanTrans注解
        if (kClass.isAnnotationPresent(BeanTrans.class)) {
            BeanTrans beanTrans = kClass.getAnnotation(BeanTrans.class);
            List<String> ignoreFiledList = Arrays.asList(beanTrans.ignoreFields());
            List<Field> fieldList = Arrays.asList(kClass.getDeclaredFields());
            //如果有需要忽略的变量，则在列表装删除该变量。
            if (CollectionUtils.isNotEmpty(ignoreFiledList)) {
                fieldList = fieldList.stream()
                        .filter(field -> !ignoreFiledList.contains(field.getName()))
                        .collect(Collectors.toList());
            }
            //循环待转换变量，通过反射一一转换得到结果。
            for (Field field : fieldList) {
                String name = field.getName();
                TransTarget transTarget = field.getAnnotation(TransTarget.class);
                String s = transTarget != null ? transTarget.value() : name;
                String sourceMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                String targetMethodName = "set" + s.substring(0, 1).toUpperCase() + s.substring(1);
                Method sourceMethod = kClass.getDeclaredMethod(sourceMethodName);
                Object invoke = sourceMethod.invoke(k);
                Method targetMethod = vClass.getDeclaredMethod(targetMethodName, field.getType());
                targetMethod.invoke(v, invoke);
            }
            return v;
        } else {
            throw new IllegalStateException("待转换对象未标注BeanTrans注解，无法正常转换。");
        }
    }

}
