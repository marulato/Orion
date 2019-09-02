package org.akita.proxy;

import org.akita.common.DefaultSchema;
import org.akita.sql.SimpleSQLGenerator;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface MybatisDAOProxy {

    @InsertProvider(type = SimpleSQLGenerator.class, method = "insert")
    void insert(DefaultSchema bean);

    @InsertProvider(type = SimpleSQLGenerator.class, method = "batchInsert")
    void batchInsert(List<DefaultSchema> beanList);

    @UpdateProvider(type = SimpleSQLGenerator.class, method = "update")
    void update(DefaultSchema bean);

    @DeleteProvider(type = SimpleSQLGenerator.class, method = "delete")
    void delete(DefaultSchema bean);

}
