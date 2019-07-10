package org.orion.common.audit.dao;

import org.apache.ibatis.annotations.*;
import org.orion.common.audit.AuditTrail;
import org.orion.common.basic.BaseEntity;
import org.orion.common.dao.SQLManager;

@Mapper
public interface AuditTrailDao {

    @Insert("UPDATE ${auditTable} SET AUDIT_TYPE = #{auditType}, AUDIT_TIME = #{auditTime} WHERE AUDIT_ID = #{auditId}")
    void updateBaseAudit(AuditTrail auditTrail);

    @InsertProvider(type = SQLManager.class, method = "createAudit")
    @Options(useGeneratedKeys = true, keyProperty = "at.auditId")
    void createAudit(@Param("at") AuditTrail auditTrail, @Param("en") BaseEntity entity);

}
