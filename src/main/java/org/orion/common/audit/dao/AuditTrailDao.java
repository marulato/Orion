package org.orion.common.audit.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.orion.common.audit.AuditTrail;
import org.orion.common.dao.DaoGenerateSvc;

@Mapper
public interface AuditTrailDao {

    @Insert("UPDATE ${auditTable} SET AUDIT_TYPE = #{auditType}, AUDIT_TIME = #{auditTime} WHERE AUDIT_ID = #{auditId}")
    void updateBaseAudit(AuditTrail auditTrail);

    @InsertProvider(type = DaoGenerateSvc.class, method = "createAudit")
    @Options(useGeneratedKeys = true, keyProperty = "auditId")
    void createAudit(AuditTrail auditTrail);

}
