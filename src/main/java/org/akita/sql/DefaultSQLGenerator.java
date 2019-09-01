package org.akita.sql;

import org.akita.common.DefaultSchema;
import org.akita.sql.api.AbstractSQLGenerator;

import java.util.List;

public class DefaultSQLGenerator extends AbstractSQLGenerator {
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
        return null;
    }

    @Override
    public String selectAll(String table) {
        return null;
    }

}
