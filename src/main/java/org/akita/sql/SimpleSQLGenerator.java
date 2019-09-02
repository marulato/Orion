package org.akita.sql;

import org.akita.common.DefaultSchema;
import org.akita.sql.api.AbstractSQLGenerator;

import java.util.List;

public class SimpleSQLGenerator extends AbstractSQLGenerator {
    @Override
    public String insert(DefaultSchema bean) {
        StringBuilder insert = new StringBuilder();
        if (bean != null) {
            insert.append("INSERT INTO ").append(bean.tableName()).append(" ");
            insert.append(insertColumns(bean)).append(" VALUES ");
            insert.append(insertValues(bean));
        }
        return insert.toString();
    }

    @Override
    public String batchInsert(List<DefaultSchema> beans) {
        StringBuilder insert = new StringBuilder();
        if (beans != null && !beans.isEmpty()) {
            insert.append("INSERT INTO ").append(beans.get(0).tableName()).append(" ");
            insert.append(insertColumns(beans.get(0))).append(" VALUES ");
            insert.append(batchValues(beans));
        }
        return insert.toString();
    }

    @Override
    public String update(DefaultSchema bean) {
        StringBuilder update = new StringBuilder();
        if (bean != null) {
            update.append("UPDATE ").append(bean.tableName()).append(" ");
            update.append(setValues(bean));
            update.append(" WHERE ");
            update.append(primaryKeyStatement(bean));
        }
        return update.toString();
    }

    @Override
    public String delete(DefaultSchema bean) {
        StringBuilder delete = new StringBuilder();
        if (bean != null) {
            delete.append("DELETE FROM ").append(bean.tableName());
            delete.append(" WHERE ").append(primaryKeyStatement(bean));
        }
        return delete.toString();
    }

    @Override
    public String selectAll(String table) {
        StringBuilder select = new StringBuilder();
        select.append("SELECT * FROM ").append(table);
        return select.toString();
    }

    @Override
    public String selectByPrimaryKey(DefaultSchema bean) {
        StringBuilder select = new StringBuilder();
        if (bean != null) {
            select.append("SELECT * FROM ").append(bean.tableName());
            select.append(" WHERE ").append(primaryKeyStatement(bean));
        }
        return select.toString();
    }

}
