package org.orion.common.dao;

import org.orion.common.miscutil.StringUtil;
import java.lang.reflect.Field;
import java.util.*;

public class BaseSQLProvider {

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

    public final String setWhereClause(String column, String operator) {
        StringBuilder where = new StringBuilder();
        where.append("WHERE ");
        where.append(column).append(" ");
        where.append(operator).append(" ");
        where.append("#{");
        where.append(StringUtil.convertColumnName(column));
        where.append("}");
        return where.toString();
    }

    public final String setWhereIn(String column, List<String> values) {
        StringBuilder where = new StringBuilder();
        where.append("WHERE ");
        where.append(column).append(" ");
        where.append("IN").append(" (");
        for (String value : values) {
            where.append(value).append(", ");
        }
        where.deleteCharAt(where.lastIndexOf(","));
        where.append(")");
        return where.toString();
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


    public final String insert(Class<?> mapperEntity, List<String> exclude, String atParam) {
        StringBuilder insert = new StringBuilder();
        if (mapperEntity != null) {
            List<String> fieldNameList = getColumnFields(mapperEntity, exclude);
            insert.append("INSERT INTO ").append(table).append(" (");
            fieldNameList.forEach((fieldName) -> {
                insert.append(StringUtil.convertToTableColumn(fieldName)).append(", ");
            });
            insert.deleteCharAt(insert.lastIndexOf(","));
            insert.append(")").append(" VALUES (");
            fieldNameList.forEach((fieldName) -> {
                if (StringUtil.isEmpty(atParam))
                    insert.append("#{" + fieldName + "}").append(", ");
                else
                    insert.append("#{"+ atParam +"." + fieldName + "}").append(", ");
            });
            insert.deleteCharAt(insert.lastIndexOf(","));
            insert.append(")");
        }
        return insert.toString();
    }

    public final String batchInsert(Class<?> mapperEntity, List<String> exclude, String atParam, int batchSize) {
        StringBuilder insert = new StringBuilder();
        batchSize = batchSize < 0 ? 0 : batchSize;
        if (mapperEntity != null && !StringUtil.isEmpty(atParam)) {
            List<String> fieldNameList = getColumnFields(mapperEntity, exclude);
            insert.append("INSERT INTO ").append(table).append(" (");
            fieldNameList.forEach((fieldName) -> {
                insert.append(StringUtil.convertToTableColumn(fieldName)).append(", ");
            });
            insert.deleteCharAt(insert.lastIndexOf(","));
            insert.append(")").append(" VALUES ");
            for (int i = 0; i < batchSize; i++) {
                insert.append("(");
                for (String name : fieldNameList) {
                    insert.append("#{" + atParam + "[" + i + "]").append("." + name).append("}");
                }
                insert.append("), ");
            }
            insert.deleteCharAt(insert.lastIndexOf(","));
        }
        return insert.toString();
    }

    public final String updateAll(Class<?> mapperEntity, List<String> exclude) {
        StringBuilder update = new StringBuilder();
        if (mapperEntity != null) {
            List<String> fieldNameList = getColumnFields(mapperEntity, exclude);
            update.append("UPDATE ").append(table).append(" SET ");
            fieldNameList.forEach((fieldName) -> {
                update.append(StringUtil.convertToTableColumn(fieldName)).append(" = ");
                update.append("#{" + fieldName + "}").append(", ");
            });
            update.deleteCharAt(update.lastIndexOf(","));
        }
        return update.toString();
    }

    public final String deleteAll() {
        StringBuilder delete = new StringBuilder();
        delete.append("DELETE FROM ").append(table).append(" ");
        return delete.toString();
    }

    public final String deleteByColumn(String byColumn, String operator) {
        StringBuilder delete = new StringBuilder();
        delete.append(deleteAll());
        delete.append(setWhereClause(byColumn, operator));
        return delete.toString();
    }

    private List<String> getColumnFields(Class<?> mapperEntity, List<String> exclude) {
        List<String> filedNameList = new ArrayList<>();
        Field[] fields = mapperEntity.getDeclaredFields();
        Arrays.stream(fields).forEach((field) -> {
            filedNameList.add(field.getName());
        });
        if (exclude != null && !exclude.isEmpty()) {
            Iterator<String> iterator = exclude.iterator();
            List<String> tableColumList = new ArrayList<>();
            while (iterator.hasNext()) {
                String ex = iterator.next();
                if (StringUtil.isTableColumn(ex)) {
                    iterator.remove();
                    tableColumList.add(StringUtil.convertColumnName(ex));
                }
            }
            exclude.addAll(tableColumList);
            filedNameList.removeAll(exclude);
            addAuditTrailColumn(filedNameList);
        }
        return filedNameList;
    }

    private void addAuditTrailColumn(List<String> fieldNameList) {
        fieldNameList.add("createdAt");
        fieldNameList.add("createdBy");
        fieldNameList.add("updatedAt");
        fieldNameList.add("updatedBy");
    }

}
