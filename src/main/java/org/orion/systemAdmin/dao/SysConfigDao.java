package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.orion.common.basic.SearchParam;
import org.orion.common.dao.SQLManager;
import org.orion.systemAdmin.entity.SystemConfig;

import java.util.List;

@Mapper
public interface SysConfigDao {

    @SelectProvider(type = SQLManager.class, method = "createSearch")
    List<SystemConfig> search(SearchParam searchParam);

    List<SystemConfig> queryAll();

    SystemConfig query(String key);

    int getCounts();

    void update(SystemConfig config);
}
