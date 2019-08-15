package org.orion.common.rbac;

import org.orion.common.basic.BaseEntity;

public class RolePermission extends BaseEntity {
    public RolePermission() {
        super("ROLE_PERMISSION", null);
    }

    private String roleId;
    private String permission;
    private String description;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
