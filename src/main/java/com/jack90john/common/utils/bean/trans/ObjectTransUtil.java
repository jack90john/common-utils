package com.jack90john.common.utils.bean.trans;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Designer: jack
 * Date: 2019-05-16
 * Version: 1.0.0
 */

public class ObjectTransUtil {

    public static <K, V> V transBean(K k, Class<V> vClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<?> kClass = k.getClass();
        V v = vClass.getDeclaredConstructor().newInstance();
        if (kClass.isAnnotationPresent(BeanTrans.class)) {
            BeanTrans beanTrans = kClass.getAnnotation(BeanTrans.class);
            List<String> ignoreFiledList = Arrays.asList(beanTrans.ignoreFields());
            List<Field> fieldList = Arrays.asList(kClass.getDeclaredFields());
            if (CollectionUtils.isNotEmpty(ignoreFiledList)) {
                fieldList = fieldList.stream()
                        .filter(field -> !ignoreFiledList.contains(field.getName()))
                        .collect(Collectors.toList());
            }
            for (Field field : fieldList) {
                String name = field.getName();
                TransTarget transTarget = field.getAnnotation(TransTarget.class);
                String s = transTarget != null ? transTarget.targetField() : name;
                String sourceMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                String targetMethodName = "set" + s.substring(0, 1).toUpperCase() + s.substring(1);
                Method sourceMethod = kClass.getDeclaredMethod(sourceMethodName);
                Object invoke = sourceMethod.invoke(k);
                Method targetMethod = vClass.getDeclaredMethod(targetMethodName, field.getType());
                targetMethod.invoke(v, invoke);
            }
        }
        return v;
    }

}
