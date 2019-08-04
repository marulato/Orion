package org.orion.common.dao;

import org.orion.common.audit.AuditTrail;
import org.orion.common.basic.BaseEntity;
import org.orion.common.basic.SearchParam;
import org.orion.common.dao.annotation.Id;
import org.orion.common.dao.annotation.SearchColumn;
import org.orion.common.miscutil.ReflectionUtil;
import org.orion.common.miscutil.SpringUtil;
import org.orion.common.miscutil.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
    public class SQLManager {
    @Resource
    private DatabaseSchemaDao schemaDao;
    private List<String> columnNames;
    private List<String> dataTypes;
    private List<String> fieldNames;
    private List<String> fieldTypes;
    private Map<String, String> nameAndTypeMap;
    private Map<String, String> columnAndFieldMap;

    private String createField(String filedName) {
        return "private " + nameAndTypeMap.get(filedName) + " " + filedName + ";";
    }

    private String createSetter(String fieldName) {
        StringBuilder setter = new StringBuilder();
        setter.append("public void set");
        setter.append(StringUtil.toUpperCaseByIndex(fieldName, 0));
        setter.append("(").append(nameAndTypeMap.get(fieldName)).append(" ");
        setter.append(fieldName).append(") ");
        setter.append("{this.").append(fieldName).append(" = ").append(fieldName);
        setter.append(";}");
        return setter.toString();
    }

    private String createGetter(String fieldName) {
        StringBuilder getter = new StringBuilder();
        getter.append("public ").append(nameAndTypeMap.get(fieldName)).append(" get");
        getter.append(StringUtil.toUpperCaseByIndex(fieldName, 0));
        getter.append("() ");
        getter.append("{return this.").append(fieldName);
        getter.append(";}");
        return getter.toString();
    }

    private String createResultAnnotation(String columnName) {
        StringBuilder result = new StringBuilder();
        result.append("@Result(");
        result.append("column = \"").append(columnName).append("\"");
        result.append(", ");
        result.append("property = \"").append(columnAndFieldMap.get(columnName)).append("\"");
        result.append("),");
        return result.toString();
    }

    public byte[] createORMEntity(String table, String className) throws Exception {
        doInitData(table);
        StringBuilder orm = new StringBuilder();
        orm.append("public class ").append(className).append(" {");
        orm.append("\n\n");
        for(String fieldName : fieldNames) {
            orm.append("\t").append(createField(fieldName)).append("\n");
        }
        orm.append("\n");
        for(String fieldName : fieldNames) {
            orm.append("\t").append(createSetter(fieldName)).append("\n\n");
            orm.append("\t").append(createGetter(fieldName)).append("\n\n");
        }
        orm.append("}");
        return orm.toString().getBytes("utf-8");
    }

    public String createORMEntity(String table) throws Exception {
        doInitData(table);
        StringBuilder orm = new StringBuilder();
        orm.append("public class ").append("DefaultClass").append(" {");
        orm.append("\n\n");
        for(String fieldName : fieldNames) {
            orm.append("\t").append(createField(fieldName)).append("\n");
        }
        orm.append("\n");
        for(String fieldName : fieldNames) {
            orm.append("\t").append(createSetter(fieldName)).append("\n\n");
            orm.append("\t").append(createGetter(fieldName)).append("\n\n");
        }
        orm.append("}");
        return orm.toString();
    }

    public String createResultsAnnotation(String table) {
        doInitData(table);
        StringBuilder anno = new StringBuilder();
        anno.append("@Results({").append("\n");
        for(String col : columnNames) {
            anno.append("\t");
            anno.append(createResultAnnotation(col));
            anno.append("\n");
        }
        anno.deleteCharAt(anno.length() - 2);
        anno.append("})");

        return anno.toString();
    }

    public String createUpdateSql(String table, String param) {
        doInitData(table);
        StringBuilder update = new StringBuilder();
        update.append("UPDATE ").append(table).append(" SET ");
        for(String col : columnNames) {
            update.append(col).append(" = ");
            if (!StringUtil.isEmpty(param))
                update.append("#{").append(param).append(".").append(columnAndFieldMap.get(col)).append("}, ");
            else
                update.append("#{").append(columnAndFieldMap.get(col)).append("}, ");
        }
        update.deleteCharAt(update.lastIndexOf(","));
        return update.toString();
    }

    public String createInsertSql(String table, String param) {
        doInitData(table);
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO ").append(table).append(" (");
        for (String col : fieldNames) {
            insert.append(StringUtil.convertToTableColumn(col));
            insert.append(", ");
        }
        insert.deleteCharAt(insert.lastIndexOf(","));
        insert.append(")");
        insert.append(" VALUES (");
        for (String col : fieldNames) {
            if (StringUtil.isEmpty(param)) {
                insert.append("#{").append(col).append("}, ");
            } else {
                insert.append("#{").append(param).append(".").append(col).append("}, ");
            }
        }
        insert.deleteCharAt(insert.lastIndexOf(","));
        insert.append(")");
        return insert.toString();
    }

    public String createInsert(BaseEntity entity) {
        if (entity != null) {
            return createInsertSql(entity.getTableName(), null);
        }
        return null;
    }

    public String createUpdate(BaseEntity entity) {
        if (entity != null) {
            StringBuilder update = new StringBuilder();
            update.append(createUpdateSql(entity.getTableName(), null));
            update.append(" WHERE ").append(appendWhere(entity));
            return update.toString();
        }
        return null;
    }

    public String createDelete(BaseEntity entity) {
        if (entity != null) {
            StringBuilder delete = new StringBuilder();
            delete.append("DELETE FROM ").append(entity.getTableName()).append(" WHERE ");
            delete.append(appendWhere(entity));
        }
        return null;
    }

    private String appendWhere(BaseEntity entity) {
        if (entity != null) {
            StringBuilder where = new StringBuilder();
            Class cls = entity.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    where.append(StringUtil.convertToTableColumn(field.getName()));
                    where.append(" = ").append("#{").append(field.getName()).append("}");
                }
                where.append(" AND ");
            }
            where.delete(where.lastIndexOf("AND"), where.length());
            return where.toString();
        }
        return null;
    }

    public String createAudit(AuditTrail<?> auditTrail) {
        StringBuilder audit = new StringBuilder();
        Object target = auditTrail.getEntity();
        DatabaseSchemaDao schemaDao = SpringUtil.getBean(DatabaseSchemaDao.class);
        List<String> columns = schemaDao.retrieveColumnNames(auditTrail.getAuditTable());
        audit.append("INSERT INTO ").append(StringUtil.convertToTableColumn(auditTrail.getAuditTable())).append(" (");
        for (String column : columns) {
            audit.append(StringUtil.convertToTableColumn(column));
            audit.append(", ");
        }
        audit.deleteCharAt(audit.lastIndexOf(","));
        audit.append(") ");
        audit.append("VALUES (NULL, ");
        for (String column : columns) {
            if ("auditId".equals(StringUtil.convertColumnName(column))) {
                continue;
            }
            if (StringUtil.convertColumnName(column).contains("audit")) {
                audit.append("#{").append(StringUtil.convertColumnName(column)).append("}, ");
            } else {
                audit.append("#{entity.").append(StringUtil.convertColumnName(column)).append("}, ");
            }
        }
        audit.deleteCharAt(audit.lastIndexOf(","));
        audit.append(")");
        return audit.toString();
    }

    public String createSearch(SearchParam searchParam) throws Exception {
        if (searchParam != null) {
            StringBuilder search = new StringBuilder();
            List<Field> searchFields = ReflectionUtil.getAnnotationFields(searchParam.getObject(), SearchColumn.class);
            search.append("SELECT * FROM ").append(searchParam.getTable());
            if (!searchFields.isEmpty()) {
                search.append(" WHERE ");
                for (Field field : searchFields) {
                    String fieldName = field.getName();
                    String columnName = StringUtil.convertToTableColumn(fieldName);
                    SearchColumn annotation = field.getAnnotation(SearchColumn.class);
                    search.append(columnName).append(" ").append(annotation.pattern().toUpperCase());
                    search.append(" CONCAT(").append(StringUtil.addSingleQuo(annotation.prefix())).append(", #{").append("object." + fieldName).append("}, ");
                    search.append(StringUtil.addSingleQuo(annotation.suffix())).append(")");
                    search.append(" AND ");
                }
                search.delete(search.lastIndexOf("AND"), search.length());
            }
            if (searchParam.getOrder() != null) {
                search.append(searchParam.getOrder().orderBy());
            }
            search.append(" LIMIT #{start}, #{pageSize}");
            return search.toString();
        }
        return null;
    }

    public String createSearchCount(SearchParam searchParam) throws Exception {
        String searchSQL = createSearch(searchParam);
        searchSQL = searchSQL.replaceFirst("\\*", "COUNT(*)");
        return searchSQL.substring(0, searchSQL.lastIndexOf("LIMIT"));
    }

    public String getCount(String tableName) {
        return "SELECT COUNT(*) FROM " + tableName;
    }

    public String executeCommand(String sql) {
        return sql;
    }

    private void doInitData(String tableName) {
        if (!StringUtil.isBlank(tableName)) {
            if (schemaDao == null) {
                schemaDao = SpringUtil.getBean(DatabaseSchemaDao.class);
            }
            columnNames = schemaDao.retrieveColumnNames(tableName);
            dataTypes = schemaDao.retrieveColumnTypes(tableName);
            columnAndFieldMap = new HashMap<>();
            if (columnNames != null && !columnNames.isEmpty() && dataTypes != null && !dataTypes.isEmpty()) {
                fieldNames = new ArrayList<>();
                fieldTypes = new ArrayList<>();
                nameAndTypeMap = new HashMap<>();
                for(String originalName : columnNames) {
                    fieldNames.add(StringUtil.convertColumnName(originalName));
                }
                for(String dataType : dataTypes) {
                    if ("varchar".equalsIgnoreCase(dataType) || "char".equalsIgnoreCase(dataType) || "text".equalsIgnoreCase(dataType)) {
                        fieldTypes.add("String");
                    } else if("double".equalsIgnoreCase(dataType)) {
                        fieldTypes.add("double");
                    } else if("int".equalsIgnoreCase(dataType)) {
                        fieldTypes.add("int");
                    } else if("bigint".equalsIgnoreCase(dataType)) {
                        fieldTypes.add("long");
                    } else if("smallint".equalsIgnoreCase(dataType)) {
                        fieldTypes.add("int");
                    } else if("date".equalsIgnoreCase(dataType) || "datetime".equalsIgnoreCase(dataType)) {
                        fieldTypes.add("Date");
                    } else if (dataType.contains("blob")) {
                        fieldTypes.add("byte[]");
                    }
                }
                for (int i = 0; i < fieldNames.size(); i++) {
                    nameAndTypeMap.put(fieldNames.get(i), fieldTypes.get(i));
                    columnAndFieldMap.put(columnNames.get(i), fieldNames.get(i));
                }

            }
        }
    }


}
