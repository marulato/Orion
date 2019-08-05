package org.orion.systemAdmin.entity;

import org.orion.common.dao.annotation.SearchColumn;
import org.orion.common.validation.annotation.Length;

public class SystemConfig {

    @SearchColumn
    private String configKey;
    @Length(min = 1, max = 64, errorCode = "002")
    @SearchColumn
    private String configValue;
    @Length(min = 1, max = 256, errorCode = "001")
    @SearchColumn
    private String description;

    public static final String TABLE_NAME       = "SYS_CONFIG";
    public static final String AUDIT_TABLE      = "SYS_CONFIG_HX";
    public static final String COL_CONFIG_KEY   = "CONFIG_KEY";
    public static final String COL_CONFIG_VALUE = "CONFIG_VALUE";
    public static final String COL_DESC         = "DESCRIPTION";

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
