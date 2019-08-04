package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.orion.common.basic.SearchParam;
import org.orion.common.dao.SQLManager;
import org.orion.systemAdmin.entity.SystemConfig;

import java.util.List;

@Mapper
public interface SysConfigDao {

    @SelectProvider(type = SQLManager.class, method = "createSearch")
    List<SystemConfig> search(SearchParam searchParam);

    List<SystemConfig> searchWithParams(@Param("sp") SearchParam<SystemConfig> searchParam);

    List<SystemConfig> queryAll();

    SystemConfig query(String key);

    int getCountsWithParams(@Param("sp") SearchParam<SystemConfig> searchParam);

    @SelectProvider(type = SQLManager.class, method = "createSearchCount")
    int getCountsBySearchParam(SearchParam<SystemConfig> searchParam);

    void update(SystemConfig config);
}
