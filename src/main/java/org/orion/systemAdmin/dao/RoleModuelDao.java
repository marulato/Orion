package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.orion.systemAdmin.entity.RoleModuelAssign;
import java.util.List;

@Mapper
public interface RoleModuelDao {

    void create(RoleModuelAssign roleModuelAssign);

    void batchCreate(@Param("rml") List<RoleModuelAssign> roleModuelAssignList);

    List<RoleModuelAssign> query(String roleId);

    List<RoleModuelAssign> queryAll();

    List<RoleModuelAssign> batchQuery(@Param("ril") List<String> roleIdList);

    void deleteAll();
}
