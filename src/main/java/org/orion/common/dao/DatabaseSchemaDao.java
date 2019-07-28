package org.orion.common.dao;

import org.apache.ibatis.annotations.*;
import org.orion.common.basic.BaseEntity;

import java.util.List;

@Mapper
public interface DatabaseSchemaDao {

    @Select("SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT database()) and TABLE_NAME = #{tn}")
    public List<String> retrieveColumnNames(@Param("tn") String tableName);

    @Select("SELECT DATA_TYPE FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT database()) and TABLE_NAME = #{tn}")
    public List<String> retrieveColumnTypes(@Param("tn") String tableName);

    @SelectProvider(type = SQLManager.class, method = "executeCommand")
    public void execute(String sql);

    @InsertProvider(type = SQLManager.class, method = "createInsert")
    public void insert(BaseEntity entity);

    @UpdateProvider(type = SQLManager.class, method = "createUpdate")
    public void update(BaseEntity entity);

    @DeleteProvider(type = SQLManager.class, method = "createDelete")
    public void delete(BaseEntity entity);
}
