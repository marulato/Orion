package org.orion.common.dao.crud;

import org.orion.common.audit.AuditTrail;
import org.orion.common.basic.BaseEntity;
import org.orion.common.dao.DatabaseSchemaDao;
import org.orion.common.miscutil.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CrudManager {

    @Resource
    private DatabaseSchemaDao databaseSchemaDao;

    public void create(BaseEntity entity) {
        if (entity != null) {
            databaseSchemaDao.insert(entity);
        }
    }

    public void update(BaseEntity entity) {
        if (entity != null) {
            databaseSchemaDao.update(entity);
        }
    }

    public String execute(String sql) {
        if (!StringUtil.isEmpty(sql)) {
           return databaseSchemaDao.execute(sql);
        }
        return null;
    }

    public int countAll(String table) {
        if (!StringUtil.isEmpty(table)) {
            return databaseSchemaDao.countAll(table);
        }
        return -1;
    }

    public void createAudit(AuditTrail auditTrail) {
        if (auditTrail != null && auditTrail.getEntity() != null) {
            databaseSchemaDao.createAudit(auditTrail);
        }
    }

    public void delete(BaseEntity baseEntity) {
        if (baseEntity != null) {
            databaseSchemaDao.delete(baseEntity);
        }
    }
}
