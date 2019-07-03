package org.orion.common.scheduel;

import org.orion.common.basic.BaseEntity;

public class BatchJobEntity extends BaseEntity {

    private String jobId;
    private String name;
    private String description;
    private String className;
    private String isQuartz;
    private String isRegistered;

    public BatchJobEntity() {
        super("BATCH_JOB_SCHEDUEL");
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
