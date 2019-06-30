package org.orion.common.document.xlsx;

import org.orion.common.miscutil.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public abstract class ExcelEntity {

    protected abstract ArrayList<String> columnOrder();

    protected abstract List<String> toRecord();

    protected ArrayList<String> toContent(ExcelEntity excelEntity) {
        ArrayList<String> content = new ArrayList<>();
        if (excelEntity != null) {
            Class cls = excelEntity.getClass();
            ArrayList<String> fieldList = columnOrder();
            Method[] methods = cls.getDeclaredMethods();
            Field[] fields = cls.getDeclaredFields();
            Map<String, Method> methodMap = new HashMap<>();
            Map<String, Field> fieldMap = new HashMap<>();
            Arrays.stream(methods).forEach(method -> {
                method.setAccessible(true);
                methodMap.put(method.getName(), method);
            });
            Arrays.stream(fields).forEach(field -> {
                field.setAccessible(true);
                fieldMap.put(field.getName(), field);
            });
            try {
                for (String field : fieldList) {
                    String method = "get" + StringUtil.toUpperCaseByIndex(field, 0);
                    Method getter = methodMap.get(method);
                    if (getter != null) {
                        content.add((String) getter.invoke(excelEntity));
                    } else {
                        content.add((String)fieldMap.get(field).get(excelEntity));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return content;
    }
}
