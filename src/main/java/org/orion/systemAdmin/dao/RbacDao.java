package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.orion.common.rbac.OrionUserRole;
import org.orion.common.rbac.RolePermission;
import org.orion.common.rbac.User;

import java.util.List;

@Mapper
public interface RbacDao {

    public List<OrionUserRole> getUserRole(User user);

    public List<RolePermission> getRolePermission(OrionUserRole orionUserRole);
}
