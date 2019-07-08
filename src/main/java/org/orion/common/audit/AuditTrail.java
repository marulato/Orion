package org.orion.common.audit;

import org.orion.common.audit.dao.AuditTrailDao;
import org.orion.common.miscutil.SpringUtil;

import java.util.Date;

public class AuditTrail {

    private String auditTable;
    private String targetTable;
    private String where;
    private long auditId;
    private String auditType;
    private Date auditTime;

    public AuditTrail(String auditTable, String auditType, String targetTable) {
        this.auditTable = auditTable;
        this.auditType = auditType;
        this.targetTable = targetTable;
        auditTime = new Date();
    }

    public void generateAudit(String where) {
        this.where = where;
        AuditTrailDao auditTrailDao = SpringUtil.getBean(AuditTrailDao.class);
        auditTrailDao.createAudit(this);
        auditTrailDao.updateBaseAudit(this);
    }

    public String getAuditTable() {
        return auditTable;
    }

    public void setAuditTable(String auditTable) {
        this.auditTable = auditTable;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
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

}
