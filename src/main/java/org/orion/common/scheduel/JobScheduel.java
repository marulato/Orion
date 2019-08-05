package org.orion.common.scheduel;

import org.orion.common.basic.BaseBatchJob;
import org.orion.common.miscutil.StringUtil;
import org.orion.common.validation.annotation.ValidateWithMethod;
import org.orion.systemAdmin.entity.AppConsts;
import org.quartz.CronExpression;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class JobScheduel {

    private Class<?> jobClass;
    @ValidateWithMethod(methodName = {"validateJobName"}, errorCode = {"002"})
    private String jobName;
    private String jobGroup;
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

    private boolean validateJobName(String jobName) {
        return !StringUtil.isEmpty(jobName) && jobName.length() <= 32;
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
            Object instance = cls.getConstructor().newInstance();
            if (AppConsts.YES.equals(automatic))
                return instance instanceof QuartzJobBean;
            else
                return instance instanceof BaseBatchJob;
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
