package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.orion.common.rbac.OrionUserRole;

import java.util.List;

@Mapper
public interface UserRoleDao {

    List<OrionUserRole> queryRole(long userId);

}
