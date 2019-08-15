package org.orion.common.rbac;

import org.orion.common.basic.BaseEntity;

public class OrionUserRole extends BaseEntity {

    private String userId;
    private String roleId;
    private String description;

    public OrionUserRole() {
        super("USER_ROLE", null);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
