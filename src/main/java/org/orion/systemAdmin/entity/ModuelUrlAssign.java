package org.orion.systemAdmin.entity;

public class ModuelUrlAssign {

    private String moduelId;
    private String moduelName;
    private String moduelDesc;
    private String funcId;
    private String funcName;
    private String level;
    private String url;
    private String isSystem;
    private String remarks;

    @Override
    public String toString() {
        return "ModuelUrlAssign{" +
                "moduelId='" + moduelId + '\'' +
                ", moduelName='" + moduelName + '\'' +
                ", moduelDesc='" + moduelDesc + '\'' +
                ", funcId='" + funcId + '\'' +
                ", funcName='" + funcName + '\'' +
                ", level='" + level + '\'' +
                ", url='" + url + '\'' +
                ", isSystem='" + isSystem + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public String getModuelId() {
        return moduelId;
    }

    public void setModuelId(String moduelId) {
        this.moduelId = moduelId;
    }

    public String getModuelName() {
        return moduelName;
    }

    public void setModuelName(String moduelName) {
        this.moduelName = moduelName;
    }

    public String getModuelDesc() {
        return moduelDesc;
    }

    public void setModuelDesc(String moduelDesc) {
        this.moduelDesc = moduelDesc;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
