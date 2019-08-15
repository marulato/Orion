package org.orion.systemAdmin.service;

import org.orion.common.rbac.OrionUserRole;
import org.orion.common.rbac.RolePermission;
import org.orion.common.rbac.User;
import org.orion.systemAdmin.dao.RbacDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RbacService {
    @Resource
    private RbacDao rbacDao;

    public List<OrionUserRole> getUserRole(User user) {
        if (user != null) {
            return rbacDao.getUserRole(user);
        }
        return new ArrayList<>();
    }

    public List<RolePermission> getRolePermission(OrionUserRole orionUserRole) {
        if (orionUserRole != null) {
            return rbacDao.getRolePermission(orionUserRole);
        }
        return new ArrayList<>();
    }
}
