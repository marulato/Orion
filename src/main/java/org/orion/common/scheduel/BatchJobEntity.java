package org.orion.common.scheduel;

import org.orion.common.basic.BaseEntity;
import org.orion.common.dao.annotation.SearchColumn;

public class BatchJobEntity extends BaseEntity {

    @SearchColumn
    private String jobName;
    @SearchColumn
    private String description;
    @SearchColumn
    private String className;
    private String isQuartz;
    private String cron;
    private String isRegistered;

    public static final String TABLE_NAME   = "BATCH_JOB_SCHEDUEL";
    public static final String AUDIT_TABLE   = "BATCH_JOB_SCHEDUEL_HX";
    public static final String COL_JOB_NAME = "JOB_NAME";

    public BatchJobEntity() {
        super(TABLE_NAME, AUDIT_TABLE);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIsQuartz() {
        return isQuartz;
    }

    public void setIsQuartz(String isQuartz) {
        this.isQuartz = isQuartz;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
