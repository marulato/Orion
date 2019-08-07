package org.orion.common.scheduel.entity;

public class BatchJobFiredHistory {

    private long fireId;
    private String jobName;
    private String fireMethod;
    private String status;
    private long fireTime;
    private long stopTime;
    private long timeCost;

    public static final String TABLE_NAME   = "BATCH_JOB_FIRED_HISTORY";

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
