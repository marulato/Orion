package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.orion.systemAdmin.entity.ModuelUrlAssign;

import java.util.List;

@Mapper
public interface ModuelUrlDao {

    void create(ModuelUrlAssign moduelUrlAssign);

    void batchCreate(@Param("mua") List<ModuelUrlAssign> moduelUrlAssignList);
}
