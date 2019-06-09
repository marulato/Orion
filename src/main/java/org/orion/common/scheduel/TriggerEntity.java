package org.orion.common.scheduel;

public class TriggerEntity {

    private String schedName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String description;
    private long nextFireTime;
    private long prevFireTime;
    private int priority;
    private String triggerState;
    private String triggerType;
    private long startTime;
    private long endTime;
    private String calendarName;
    private int misfireInstr;
    private byte[] jobData;

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getSchedName() {
        return this.schedName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerGroup() {
        return this.triggerGroup;
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

    public void setNextFireTime(long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public long getNextFireTime() {
        return this.nextFireTime;
    }

    public void setPrevFireTime(long prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public long getPrevFireTime() {
        return this.prevFireTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getTriggerState() {
        return this.triggerState;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerType() {
        return this.triggerType;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public String getCalendarName() {
        return this.calendarName;
    }

    public void setMisfireInstr(int misfireInstr) {
        this.misfireInstr = misfireInstr;
    }

    public int getMisfireInstr() {
        return this.misfireInstr;
    }

    public void setJobData(byte[] jobData) {
        this.jobData = jobData;
    }

    public byte[] getJobData() {
        return this.jobData;
    }
}
