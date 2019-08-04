package org.orion.systemAdmin.entity;

import org.orion.common.dao.annotation.SearchColumn;
import org.orion.common.miscutil.StringUtil;
import org.orion.common.validation.ValidateWithMethod;

public class SystemConfig {

    @SearchColumn
    private String configKey;
    @ValidateWithMethod(methodName = {"validateValue"}, errorCode = {"002"})
    @SearchColumn
    private String configValue;
    @ValidateWithMethod(methodName = {"validateDesc"}, errorCode = {"001"})
    @SearchColumn
    private String description;

    public static final String TABLE_NAME       = "SYS_CONFIG";
    public static final String AUDIT_TABLE      = "SYS_CONFIG_HX";
    public static final String COL_CONFIG_KEY   = "CONFIG_KEY";
    public static final String COL_CONFIG_VALUE = "CONFIG_VALUE";
    public static final String COL_DESC         = "DESCRIPTION";

    private boolean validateValue(String value) {
        return !StringUtil.isEmpty(value) && value.length() <= 64;
    }

    private boolean validateDesc(String desc) {
        return !StringUtil.isEmpty(desc) && desc.length() <= 256;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
