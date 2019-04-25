package org.orion.common.basic;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable {
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
    private String createdAtStr;
    private String updatedAtStr;
    private String createdAtFullStr;
    private String updatedAtFullStr;

    public void setAudit(String createdBy, Date createdAt, String updatedBy, Date updatedAt) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public  void setAudit(String by, Date at) {
        setAudit(by, at, by, at);
    }

    public void setUpdateAudit(String  updatedBy, Date updatedAt) {
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
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
}
