package org.orion.common.schedule.entity;

import org.orion.common.basic.BaseBatchJob;
import org.orion.common.basic.BaseEntity;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.validation.annotation.Length;
import org.orion.common.validation.annotation.ValidateWithMethod;
import org.orion.systemAdmin.entity.AppConsts;
import org.quartz.CronExpression;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class JobSchedule extends BaseEntity {

    private Class<?> jobClass;
    @Length(min = 1, max = 32, errorCode = "003")
    private String jobName;
    @Length(min = 1, max = 32, errorCode = "004")
    private String jobGroup;
    @Length(min = 0, max = 256, errorCode = "001")
    private String jobDesc;
    private String triggerName;
    private String triggerGroup;
    private String triggerDesc;
    @ValidateWithMethod(methodName = {"validateCron"}, errorCode = {"007"})
    private String cron;

    //additional fields
    private String automatic;
    private String register;
    @ValidateWithMethod(methodName = {"validateClass"}, errorCode = {"006"})
    private String className;
    private BatchJobFiredHistory firedHistory;
    private String lastFireDate;

    public JobSchedule() {
        super(null, null);
    }

    private boolean validateCron(String cron) {
        if (AppConsts.YES.equals(automatic)) {
            return CronExpression.isValidExpression(cron);
        }
        return true;
    }

    private boolean validateClass(String className)  {
        try {
            Class cls = Class.forName(className);
            if (AppConsts.YES.equals(automatic))
                return cls.getSuperclass() == QuartzJobBean.class;
            else
                return cls.getSuperclass() == BaseBatchJob.class;
        } catch (Exception e) {
            return false;
        }
    }

    public Class getJobClass() throws Exception {
        if (jobClass == null) {
            jobClass = Class.forName(className);
        }
        return jobClass;
    }

    public BatchJobFiredHistory getFiredHistory() {
        return firedHistory;
    }

    public void setFiredHistory(BatchJobFiredHistory firedHistory) {
        this.firedHistory = firedHistory;
        if (firedHistory != null) {
            lastFireDate = DateUtil.getStandardDate(new Date(firedHistory.getFireTime()));
        }
    }

    public String getLastFireDate() {
        return lastFireDate;
    }

    public void setLastFireDate(String lastFireDate) {
        this.lastFireDate = lastFireDate;
    }

    public void setJobClass(Class<?> jobClass) {
        this.jobClass = jobClass;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerDesc() {
        return triggerDesc;
    }

    public void setTriggerDesc(String triggerDesc) {
        this.triggerDesc = triggerDesc;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getAutomatic() {
        return automatic;
    }

    public void setAutomatic(String automatic) {
        this.automatic = automatic;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
