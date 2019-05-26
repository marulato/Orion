package org.orion.common.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DatabaseSchemaDao {

    @Select("SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT database()) and TABLE_NAME = #{tn}")
    public List<String> retrieveColumnNames(@Param("tn") String tableName);

    @Select("SELECT DATA_TYPE FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = (SELECT database()) and TABLE_NAME = #{tn}")
    public List<String> retrieveColumnTypes(@Param("tn") String tableName);
}
