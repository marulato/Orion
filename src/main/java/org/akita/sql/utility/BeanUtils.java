package org.akita.sql.utility;

import org.akita.crud.annotation.Id;
import org.akita.sql.converter.PropertyConverter;

import java.lang.reflect.Field;

public class BeanUtils {

    private static PropertyConverter converter = PropertyConverter.getInstance();

    public static Field[] getFields(Object bean) {
        if (bean != null) {
            Class beanType = bean.getClass();
            Field[] fields = beanType.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            return fields;
        }
        return new Field[0];
    }

    public static String[] getPropertyNames(Object bean) {
        Field[] fields = getFields(bean);
        String[] names = new String[fields.length];
        if (fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                names[i] = fields[i].getName();
            }
        }
        return names;
    }

    public static String[] getIdPropName(Object bean) {
        Field[] fields = getFields(bean);
        StringBuilder props = new StringBuilder();
        for(Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                props.append(field.getName());
                props.append(" ");
            }
        }
        props.deleteCharAt(props.lastIndexOf(" "));
        return props.toString().split(" ");
    }
}
