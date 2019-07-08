package org.orion.systemAdmin.entity;

import org.orion.common.basic.BaseEntity;

public class MasterCode extends BaseEntity {

    private int mcId;
    private String codeType;
    private String code;
    private String description;

    public MasterCode() {
        super("MASTER_CODE_MC", "MASTER_CODE_MC_HX");
    }

    public int getMcId() {
        return mcId;
    }

    public void setMcId(int mcId) {
        this.mcId = mcId;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
