package com.jack90john.common.utils.bean.trans;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对象转换工具
 *
 * @author jack
 * @version 1.2.0
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
     * @param k 待转换对象，需要标注{@link BeanTrans}注解
     * @param vClass 结果对象class
     * @param <K> 待转换对象类型
     * @param <V> 结果对象类型
     * @return 转换后得到待对象
     * @throws IllegalAccessException 反射异常
     * @throws InvocationTargetException 反射异常
     * @see IgnoreFields 需要忽略字段
     * @see TransTarget 指定目标变量
     * @since 1.0.0.RELEASE
     */
    public static <K, V> V transBean(K k, Class<V> vClass) throws IllegalAccessException, InvocationTargetException {

        Class<?> kClass = k.getClass();
        List<Field> fieldList = Arrays.asList(kClass.getDeclaredFields());
        // 检查是否标注BeanTrans注解，已过时
        fieldList = checkBeanTrans(kClass, fieldList);
        if (kClass.isAnnotationPresent(IgnoreFields.class)) {
            IgnoreFields ignoreFields = kClass.getAnnotation(IgnoreFields.class);
            List<String> ignoreFiledList = Arrays.asList(ignoreFields.value());
            if (CollectionUtils.isNotEmpty(ignoreFiledList)) {
                fieldList = fieldList.stream()
                        .filter(field -> !ignoreFiledList.contains(field.getName()))
                        .collect(Collectors.toList());
            }
        }
        //循环待转换变量，通过反射一一转换得到结果。
        V v = null;
        try {
            v = vClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new ObjectTransException("无法实例化抽象类或接口。", e);
        } catch (NoSuchMethodException e) {
            throw new ObjectTransException(vClass.getName() + "无默认构造方法。", e);
        }
        for (Field field : fieldList) {
            String name = field.getName();
            TransTarget transTarget = field.getAnnotation(TransTarget.class);
            String s = transTarget != null ? transTarget.value() : name;
            Field targetField;
            try {
                targetField = vClass.getDeclaredField(s);
            } catch (NoSuchFieldException e) {
                continue;
            }
            if (field.getType().equals(targetField.getType())) {
                String sourceMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                String targetMethodName = "set" + s.substring(0, 1).toUpperCase() + s.substring(1);
                Method sourceMethod;
                Method targetMethod;
                try {
                    sourceMethod = kClass.getDeclaredMethod(sourceMethodName);
                    targetMethod = vClass.getDeclaredMethod(targetMethodName, field.getType());
                } catch (NoSuchMethodException e) {
                    throw new ObjectTransException("无法获取到对应get/set方法。", e);
                }
                Object invoke = sourceMethod.invoke(k);
                targetMethod.invoke(v, invoke);
            }
        }
        return v;
    }

    /**
     * 兼容已过时注解，随时可能删除
     * @param kClass 待转换对象类型
     * @param fieldList 变量列表
     * @return 待转换变量列表
     * @see BeanTrans 已过时注解
     * @since 2.1.0.RELEASE
     */
    @Deprecated
    private static List<Field> checkBeanTrans(Class<?> kClass, List<Field> fieldList) {
        if (kClass.isAnnotationPresent(BeanTrans.class)) {
            BeanTrans beanTrans = kClass.getAnnotation(BeanTrans.class);
            List<String> ignoreFiledList = Arrays.asList(beanTrans.ignoreFields());
            //如果有需要忽略的变量，则在列表装删除该变量。
            if (CollectionUtils.isNotEmpty(ignoreFiledList)) {
                return fieldList.stream()
                        .filter(field -> !ignoreFiledList.contains(field.getName()))
                        .collect(Collectors.toList());
            }
        }
        return fieldList;
    }
}
