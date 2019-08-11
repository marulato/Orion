package org.orion.common.basic;

import org.orion.common.miscutil.DateUtil;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseEntity implements Serializable, Cloneable {
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
    private String createdAtStr;
    private String updatedAtStr;
    private String createdAtFullStr;
    private String updatedAtFullStr;
    private String tableName;
    private String auditTable;

    public BaseEntity(String tableName, String auditTable) {
        this.auditTable = auditTable;
        this.tableName = tableName;
    }

    public void setAudit(String createdBy, Date createdAt, String updatedBy, Date updatedAt) {
        setCreatedAt(createdAt);
        this.createdBy = createdBy;
        setUpdatedAt(updatedAt);
        this.updatedBy = updatedBy;
    }

    public  void setAudit(String by, Date at) {
        setAudit(by, at, by, at);
    }

    public void setUpdateAudit(String  updatedBy, Date updatedAt) {
        this.updatedBy = updatedBy;
        setUpdatedAt(updatedAt);
    }

    public void setCreateAudit(String  createdBy, Date createdAt) {
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        createdAtStr = DateUtil.getStandardDate(createdAt);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        updatedAtStr = DateUtil.getStandardDate(updatedAt);
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedAtStr() {
        return createdAtStr;
    }

    public void setCreatedAtStr(String createdAtStr) {
        this.createdAtStr = createdAtStr;
    }

    public String getUpdatedAtStr() {
        return updatedAtStr;
    }

    public void setUpdatedAtStr(String updatedAtStr) {
        this.updatedAtStr = updatedAtStr;
    }

    public String getCreatedAtFullStr() {
        return createdAtFullStr;
    }

    public void setCreatedAtFullStr(String createdAtFullStr) {
        this.createdAtFullStr = createdAtFullStr;
    }

    public String getUpdatedAtFullStr() {
        return updatedAtFullStr;
    }

    public void setUpdatedAtFullStr(String updatedAtFullStr) {
        this.updatedAtFullStr = updatedAtFullStr;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAuditTable() {
        return auditTable;
    }

    public void setAuditTable(String auditTable) {
        this.auditTable = auditTable;
    }
}
