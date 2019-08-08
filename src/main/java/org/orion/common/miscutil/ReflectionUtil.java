package org.orion.common.miscutil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    public static List<Field> getAnnotationFields(Object target, Class annotationType) {
        List<Field> fieldsWithAnno = new ArrayList<>();
        if (target != null && annotationType != null) {
            Class targetClass = target.getClass();
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(annotationType)) {
                    fieldsWithAnno.add(field);
                }
            }
        }
        return fieldsWithAnno;
    }

    public static List<String> getAnnotationFieldNames(Object target, Class annotationType) {
        List<String> fieldNames = new ArrayList<>();
        List<Field> fieldList = getAnnotationFields(target, annotationType);
        if (!fieldList.isEmpty()) {
            for (Field field : fieldList) {
                fieldNames.add(field.getName());
            }
        }
        return fieldNames;
    }

    public static String getGetter(Field field) {
        if (field != null) {
            return "get" + StringUtil.toUpperCaseByIndex(field.getName(), 0);
        }
        return null;
    }

    public static String getSetter(Field field) {
        if (field != null) {
            return "set" + StringUtil.toUpperCaseByIndex(field.getName(), 0);
        }
        return null;
    }

    public static Object invokeGetter(Object target, Field field) throws Exception {
        Class targetClass = target.getClass();
        Method getter = targetClass.getDeclaredMethod(getGetter(field));
        getter.setAccessible(true);
        return getter.invoke(target);
    }

    public static String getString(Object target, Field field) throws Exception {
        if (field != null) {
            field.setAccessible(true);
            Object object =invokeGetter(target, field);
            if (object instanceof String) {
                return (String) object;
            }
        }
        return null;
    }

    public static String getString(Object target, String fieldName) throws Exception {
        if (target != null && !StringUtil.isEmpty(fieldName)) {
            Class targetClass = target.getClass();
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(target);
            if (value instanceof String) {
                return (String) value;
            }
        }
        return null;
    }

    public static List<Field> getNonstaticField(Object target, Class<?> type) {
        List<Field> specifiedField = new ArrayList<>();
        if (target != null && type != null) {
            Class targetClass = target.getClass();
            Field[] allFields = targetClass.getDeclaredFields();
            for (Field field : allFields) {
                if (field.getType() == type) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    specifiedField.add(field);
                }
            }
        }
        return specifiedField;
    }
}
