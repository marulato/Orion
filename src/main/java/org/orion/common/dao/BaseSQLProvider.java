package org.orion.common.dao;

import org.orion.common.miscutil.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class BaseSQLProvider {

    private String table;

    public BaseSQLProvider(String table) {
        this.table = table;
    }

    public final String selectAll() {
        StringBuilder select = new StringBuilder();
        select.append("SELECT * FROM ");
        select.append(table).append(" ");
        return select.toString();
    }

    public final String selectAll(List<String> columns) {
        StringBuilder select = new StringBuilder();
        if (columns != null && !columns.isEmpty()) {
            select.append("SELECT ");
            columns.forEach((col) ->{
                select.append(col).append(", ");
            });
            select.deleteCharAt(select.lastIndexOf(","));
            select.append("FROM ");
        } else {
            select.append("SELECT * FROM ");
        }
        select.append(table).append(" ");
        return select.toString();
    }

    public final String addWhereAnd(String column, String operator) {
        StringBuilder where = new StringBuilder();
        where.append(" AND ");
        where.append(column).append(" ");
        where.append(operator).append(" ");
        where.append("#{");
        where.append(StringUtil.convertColumnName(column));
        where.append("}");
        return where.toString();
    }

    public final String addWhereOr(String column, String operator) {
        StringBuilder where = new StringBuilder();
        where.append(" OR ");
        where.append(column).append(" ");
        where.append(operator).append(" ");
        where.append("#{");
        where.append(StringUtil.convertColumnName(column));
        where.append("}");
        return where.toString();
    }

    public final String orderByDesc(System column) {
        StringBuilder order = new StringBuilder();
        order.append(" ORDER BY ");
        order.append(column);
        order.append(" DESC");
        return order.toString();
    }

    public final String orderByAsc(System column) {
        StringBuilder order = new StringBuilder();
        order.append(" ORDER BY ");
        order.append(column);
        order.append(" ASC");
        return order.toString();
    }

    public abstract List<String> excludeForInsert();

    public final String insertAll(Class<?> mapperEntity) {
        StringBuilder insert = new StringBuilder();
        if (mapperEntity != null) {
            Field[] fields = mapperEntity.getDeclaredFields();
            List<String> filedNameList = new ArrayList<>();
            Arrays.stream(fields).forEach((field) -> {
                filedNameList.add(field.getName());
            });
            List<String> exclude = excludeForInsert();
            if (exclude != null && !exclude.isEmpty()) {
                Iterator<String> iterator = exclude.iterator();
                List<String> tableColumList = new ArrayList<>();
                while (iterator.hasNext()) {
                    String ex = iterator.next();
                    if (StringUtil.isTableColumn(ex));
                        iterator.remove();
                        tableColumList.add(StringUtil.convertColumnName(ex));
                }
                exclude.addAll(tableColumList);
                filedNameList.removeAll(exclude);
            }
            insert.append("INSERT INTO ").append(table).append(" (");
            filedNameList.forEach((fieldName) -> {
                insert.append(StringUtil.convertToTableColumn(fieldName)).append(", ");
            });
            insert.deleteCharAt(insert.lastIndexOf(","));
            insert.append(")").append(" VALUES (");
            filedNameList.forEach((fieldName) -> {
                insert.append("#{" + fieldName + "}").append(", ");
            });
            insert.deleteCharAt(insert.lastIndexOf(","));
            insert.append(")");
        } else {
            insert.delete(0, insert.length() - 1);
        }
        return insert.toString();
    }

}
