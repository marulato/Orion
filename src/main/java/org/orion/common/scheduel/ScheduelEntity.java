package org.orion.common.scheduel;

import java.util.Arrays;

public class ScheduelEntity {

    private String schedName;
    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
    private String isDurable;
    private String isNonconcurrent;
    private String isUpdateData;
    private String requestsRecovery;
    private byte[] jobData;


    @Override
    public String toString() {
        return "ScheduelEntity{" +
                "schedName='" + schedName + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", description='" + description + '\'' +
                ", jobClassName='" + jobClassName + '\'' +
                ", isDurable='" + isDurable + '\'' +
                ", isNonconcurrent='" + isNonconcurrent + '\'' +
                ", isUpdateData='" + isUpdateData + '\'' +
                ", requestsRecovery='" + requestsRecovery + '\'' +
                ", jobData=" + Arrays.toString(jobData) +
                '}';
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getSchedName() {
        return this.schedName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobGroup() {
        return this.jobGroup;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getJobClassName() {
        return this.jobClassName;
    }

    public void setIsDurable(String isDurable) {
        this.isDurable = isDurable;
    }

    public String getIsDurable() {
        return this.isDurable;
    }

    public void setIsNonconcurrent(String isNonconcurrent) {
        this.isNonconcurrent = isNonconcurrent;
    }

    public String getIsNonconcurrent() {
        return this.isNonconcurrent;
    }

    public void setIsUpdateData(String isUpdateData) {
        this.isUpdateData = isUpdateData;
    }

    public String getIsUpdateData() {
        return this.isUpdateData;
    }

    public void setRequestsRecovery(String requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }

    public String getRequestsRecovery() {
        return this.requestsRecovery;
    }

    public void setJobData(byte[] jobData) {
        this.jobData = jobData;
    }

    public byte[] getJobData() {
        return this.jobData;
    }
}
