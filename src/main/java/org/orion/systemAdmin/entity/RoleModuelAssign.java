package org.orion.systemAdmin.entity;

import org.orion.common.miscutil.StringUtil;
import org.orion.common.validation.annotation.ValidateWithMethod;

import java.util.Objects;

public class RoleModuelAssign implements BaseAssign {

    @ValidateWithMethod(methodName = {"validateRoleId"}, errorCode = {"000"})
    private String roleId;
    @ValidateWithMethod(methodName = {"validateModuelId"}, errorCode = {"000"})
    private String moduelId;
    private String funcId;

    private boolean validateRoleId(String roleId) {
        boolean isValid = false;
        if (!StringUtil.isEmpty(roleId)) {
            if (roleId.matches("[A-Z]{3,15}"))
                isValid = true;
        }
        return isValid;
    }

    private boolean validateModuelId(String moduelId) {
        boolean isValid = false;
        if (!StringUtil.isEmpty(moduelId)) {
            if (moduelId.matches("[a-zA-Z]{3,64}"))
                isValid = true;
        }
        return isValid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleModuelAssign)) return false;
        RoleModuelAssign that = (RoleModuelAssign) o;
        return getRoleId().equals(that.getRoleId()) &&
                getModuelId().equals(that.getModuelId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getModuelId());
    }


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getModuelId() {
        return moduelId;
    }

    public void setModuelId(String moduelId) {
        this.moduelId = moduelId;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }
}
