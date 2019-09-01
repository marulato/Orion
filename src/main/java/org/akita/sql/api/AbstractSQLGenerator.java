package org.akita.sql.api;

import org.akita.common.DefaultSchema;
import org.akita.sql.converter.PropertyConverter;
import org.akita.sql.utility.BeanUtils;

import java.util.List;

public abstract class AbstractSQLGenerator {

    private PropertyConverter converter = PropertyConverter.getInstance();

    public String placeholder(String property) {
        StringBuilder holder = new StringBuilder();
        holder.append("#{").append(property).append("}");
        return holder.toString();
    }

    public String insertColumns(Object bean) {
        StringBuilder column = new StringBuilder();
        if (bean != null) {
            column.append("(");
            String[] propNames = BeanUtils.getPropertyNames(bean);
            for (String propName : propNames) {
                column.append(converter.getColumn(propName));
                column.append(", ");
            }
            column.delete(column.length() - 2, column.length());
            column.append(")");
        }
        return column.toString();
    }

    public String insertValues(Object rowMapper) {
        StringBuilder values = new StringBuilder();
        if (rowMapper != null) {
            values.append("(");
            String[] propNames = BeanUtils.getPropertyNames(rowMapper);
            for (String propName : propNames) {
                values.append(placeholder(propName));
                values.append(", ");
            }
            values.delete(values.length() - 2, values.length());
            values.append(")");
        }
        return values.toString();
    }

    public String batchValues(List<?> rowMappers) {
        StringBuilder values = new StringBuilder();
        for (Object rowMapper : rowMappers) {
            values.append(insertValues(rowMapper));
            values.append(", ");
        }
        values.delete(values.length() - 2, values.length());
        return values.toString();
    }

    public String setValues(Object rowMapper) {
        StringBuilder set = new StringBuilder();
        String[] propNames = BeanUtils.getPropertyNames(rowMapper);
        set.append("SET ");
        for (String propName : propNames) {
            set.append(converter.getColumn(propName));
            set.append(" = ");
            set.append(placeholder(propName));
            set.append(", ");
        }
        set.delete(set.length() - 2, set.length());
        return set.toString();
    }

    public String whereStatement(Object rowMapper) {
        StringBuilder where = new StringBuilder();
        String[] idProp = BeanUtils.getIdPropName(rowMapper);
        for (String id : idProp) {
            where.append(converter.getColumn(id));
            where.append(" = ");
            where.append(placeholder(id));
            where.append(" AND ");
        }
        where.deleteCharAt(where.lastIndexOf(" AND "));
        return where.toString();
    }

    public abstract String insert(DefaultSchema bean);
    public abstract String batchInsert(List<DefaultSchema> beans);
    public abstract String update(DefaultSchema bean);
    public abstract String selectAll(String table);

}
