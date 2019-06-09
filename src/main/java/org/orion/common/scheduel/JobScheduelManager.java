package org.orion.common.scheduel;

import org.orion.common.basic.AppContext;
import org.orion.common.basic.BaseBatchJob;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.StringUtil;
import org.orion.common.scheduel.dao.JobScheduelDao;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

@Service
public final class JobScheduelManager {

    @Resource
    private Scheduler scheduler;
    @Resource
    private JobScheduelDao jobScheduelDao;
    private final Logger logger = LoggerFactory.getLogger(JobScheduelManager.class);

    private void registerJob(Class<? extends QuartzJobBean> batchJobClass, String cron, String jobId, String groupId,
                          String triggerId, String triggerGroup, String jobDesc, String triggerDesc) {
        if (batchJobClass == null || StringUtil.isEmpty(cron) || StringUtil.isEmpty(jobId) || StringUtil.isEmpty(groupId)) {
            logger.warn("");
            return;
        }
        if (StringUtil.isEmpty(triggerId))
            triggerId = jobId;
        if (StringUtil.isEmpty(triggerGroup))
            triggerGroup = groupId;
        try {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerId, triggerGroup);
            CronTrigger triggerOld = null;
            triggerOld = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (triggerOld != null) {
                logger.warn("Batch Job already EXSITS");
                return;
            } else {
                JobDetail jobDetail = JobBuilder.newJob(batchJobClass).withIdentity(jobId, groupId).build();
                JobDetailImpl finalJob = (JobDetailImpl) jobDetail;
                finalJob.setDescription(jobDesc);
                CronTrigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(triggerId, triggerGroup).
                        withSchedule(cronScheduleBuilder).build();
                CronTriggerImpl finalTrigger = (CronTriggerImpl) trigger;
                finalTrigger.setMisfireInstruction(CronTriggerImpl.MISFIRE_INSTRUCTION_DO_NOTHING);
                finalTrigger.setDescription(triggerDesc);
                scheduler.scheduleJob(finalJob, finalTrigger);
            }

        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public OrionBatchJobEntity getOrionBatchJob(JobScheduel jobScheduel, boolean isQuartz, boolean isRegistered) {
        if (jobScheduel != null) {
            OrionBatchJobEntity batchJobEntity = new OrionBatchJobEntity();
            batchJobEntity.setName(jobScheduel.getName());
            batchJobEntity.setClassName(jobScheduel.getJobClass().getName());
            batchJobEntity.setDescription(jobScheduel.getJobDesc());
            batchJobEntity.setIsQuartz(isQuartz ? "Y" : "N");
            batchJobEntity.setIsRegistered(isRegistered ? "Y" : "N");
            return batchJobEntity;
        }
        return null;
    }

    public void registerAutoJob(JobScheduel jobScheduel) {
        registerJob(jobScheduel.getJobClass(), jobScheduel.getCron(), jobScheduel.getJobId(), jobScheduel.getGroupId(),
                jobScheduel.getTriggerId(), jobScheduel.getTriggerGroup(), jobScheduel.getJobDesc(), jobScheduel.getTriggerDesc());
    }

    public void registerAutoJobNow(JobScheduel jobScheduel, AppContext appContext) {
        if (jobScheduel != null && appContext != null) {
            registerAutoJob(jobScheduel);
            OrionBatchJobEntity batchJobEntity = getOrionBatchJob(jobScheduel, true, true);
            batchJobEntity.setAudit(appContext.getUserId(), DateUtil.now());
            jobScheduelDao.createOrionBatchJob(batchJobEntity);
        }
    }

    public void registerAutoJobDelay(JobScheduel jobScheduel, AppContext appContext) {
        if (jobScheduel != null && appContext != null) {
            registerAutoJob(jobScheduel);
            OrionBatchJobEntity batchJobEntity = getOrionBatchJob(jobScheduel, true, false);
            batchJobEntity.setAudit(appContext.getUserId(), DateUtil.now());
            jobScheduelDao.createOrionBatchJob(batchJobEntity);
        }
    }

    public void startNow() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("计划任务启动失败", e);
        }
    }

    public void deleteJob(String triggerName, String triggerGroup, String jobId, String groupId) {
        try {
            if (StringUtil.isEmpty(triggerGroup) || StringUtil.isEmpty(triggerName)) {
                deleteJob(jobId, groupId);
            } else if (!StringUtil.isEmpty(jobId) && !StringUtil.isEmpty(groupId)) {
                TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                JobKey jobKey = JobKey.jobKey(jobId, groupId);
                scheduler.deleteJob(jobKey);
            } else {
                logger.warn("Job Identity is empty, deletion abort");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void deleteJob(String jobId, String groupId) {
        if (StringUtil.isEmpty(jobId) || StringUtil.isEmpty(groupId)) {
            logger.warn("");
            return;
        }
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobId, groupId);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            JobKey jobKey = JobKey.jobKey(jobId, groupId);
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void modifyJob(String jobId, String groupId, String cron) {
        try {
            TriggerKey oldTriggerKey = TriggerKey.triggerKey(jobId, groupId);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(oldTriggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(oldTriggerKey, cronTrigger);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void modifyJob(JobScheduel jobScheduel) {
        if (jobScheduel != null) {
            try {
                TriggerKey oldTriggerKey = TriggerKey.triggerKey(jobScheduel.getJobId(), jobScheduel.getGroupId());
                if (oldTriggerKey == null) {
                    oldTriggerKey = TriggerKey.triggerKey(jobScheduel.getTriggerId(), jobScheduel.getTriggerGroup());
                }
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobScheduel.getCron());
                CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(oldTriggerKey).withSchedule(scheduleBuilder).build();
                scheduler.rescheduleJob(oldTriggerKey, cronTrigger);

            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    public void pauseJob(String jobId, String groupId) {
        try {
            JobKey jobKey = JobKey.jobKey(jobId, groupId);
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void resumeJob(String jobId, String groupId) {
        try {
            JobKey jobKey = JobKey.jobKey(jobId, groupId);
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public boolean isStarted() {
        try {
            return scheduler.isStarted();
        } catch (SchedulerException e) {
            logger.error("", e);
        }
        return false;
    }

    public boolean isRegistered(String jobName) {
        if (!StringUtil.isEmpty(jobName)) {
            OrionBatchJobEntity batchJobEntity = jobScheduelDao.queryOrionJob(jobName);
            if (batchJobEntity != null) {
                return batchJobEntity.getIsRegistered().equals("Y");
            }
        }
        return false;
    }

    public void executeJob(String className, String jobId) {
        if (!StringUtil.isEmpty(className) && !StringUtil.isEmpty(jobId)) {
            try {
                Class jobClass = Class.forName(className);
                Object instance = jobClass.getConstructor(String.class).newInstance(jobId);
                if (instance instanceof BaseBatchJob) {
                    Class baseJobClass = jobClass.getSuperclass();
                    Method method = baseJobClass.getDeclaredMethod("start");
                    method.setAccessible(true);
                    method.invoke(instance);
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    public List<OrionBatchJobEntity> getAllOrionJobs() {
        return jobScheduelDao.queryAllOrionJobs();
    }

    public List<ScheduelEntity> searchJobScheduels(int page, int pageSize) {
        return jobScheduelDao.queryAllJobs(page, pageSize);
    }
}
