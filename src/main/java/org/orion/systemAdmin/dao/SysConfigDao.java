package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.orion.systemAdmin.entity.SystemConfig;

import java.util.List;

@Mapper
public interface SysConfigDao {

    List<SystemConfig> search(int page, int pageSize);

    List<SystemConfig> queryAll();


    SystemConfig query(String key);

    int getCounts();

    void update(SystemConfig config);
}
