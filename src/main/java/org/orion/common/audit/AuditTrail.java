package org.orion.common.audit;

import org.orion.common.audit.dao.AuditTrailDao;
import org.orion.common.basic.BaseEntity;
import org.orion.common.miscutil.SpringUtil;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditTrail {

    private String auditTable;
    private String targetTable;
    private String where;
    private long auditId;
    private String auditType;
    private Date auditTime;
    private List<String> auditKeys;

    public AuditTrail(String auditType) {
        auditKeys = new ArrayList<>();
        this.auditType = auditType;
        auditTime = new Date();
    }

    @Transactional
    public void generateAudit(BaseEntity entity) {
        if (auditKeys != null && !auditKeys.isEmpty() && entity != null) {
            auditTable = entity.getAuditTable();
            targetTable = entity.getTableName();
            Class cls = entity.getClass();
            Field[] fields = cls.getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (field.isAnnotationPresent(AuditId.class)) {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        auditKeys.add(fieldName);
                    }
                }
            } catch (Exception e) {

            }
            AuditTrailDao auditTrailDao = SpringUtil.getBean(AuditTrailDao.class);
            auditTrailDao.createAudit(this, entity);
            auditTrailDao.updateBaseAudit(this);
        }
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

    public List<String> getAuditKeys() {
        return auditKeys;
    }

    public void setAuditKeys(List<String> auditKeys) {
        this.auditKeys = auditKeys;
    }
}
