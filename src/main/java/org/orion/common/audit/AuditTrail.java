package org.orion.common.audit;

import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.ReflectionUtil;

import java.util.Date;

public class AuditTrail <T> {

    private String auditTable;
    private long auditId;
    private String auditType;
    private Date auditTime;
    private T entity;

    public AuditTrail (T entity, String auditType) {
        try {
            auditTable = ReflectionUtil.getString(entity, "AUDIT_TABLE");
        } catch (Exception e) {
            try {
                auditTable = ReflectionUtil.getString(entity, "TABLE_NAME") + "_HX";
            } catch (Exception e1) {
                auditTable = null;
            }
        }
        this.entity = entity;
        this.auditType = auditType;
        this.auditTime = DateUtil.now();
    }

    public String getAuditTable() {
        return auditTable;
    }

    public void setAuditTable(String auditTable) {
        this.auditTable = auditTable;
    }

    public long getAuditId() {
        return auditId;
    }

    public void setAuditId(long auditId) {
        this.auditId = auditId;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
