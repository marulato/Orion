package org.orion.common.schedule.entity;

import org.orion.common.basic.BaseEntity;
import org.orion.common.dao.annotation.Id;

public class BatchJobFiredHistory extends BaseEntity {

    @Id
    private long fireId;
    private String jobName;
    private String jobGroup;
    private String fireMethod;
    private String status;
    private long fireTime;
    private long stopTime;
    private long timeCost;

    public static final String TABLE_NAME   = "BATCH_JOB_FIRED_HISTORY";

    public BatchJobFiredHistory() {
        super(TABLE_NAME, null);
    }

    public void setFireId(long fireId) {
        this.fireId = fireId;
    }

    public long getFireId() {
        return this.fireId;
    }

    public void setFireMethod(String fireMethod) {
        this.fireMethod = fireMethod;
    }

    public String getFireMethod() {
        return this.fireMethod;
    }

    public void setFireTime(long fireTime) {
        this.fireTime = fireTime;
    }

    public long getFireTime() {
        return this.fireTime;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return this.jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public long getStopTime() {
        return this.stopTime;
    }

    public void setTimeCost(long timeCost) {
        this.timeCost = timeCost;
    }

    public long getTimeCost() {
        return this.timeCost;
    }

}
