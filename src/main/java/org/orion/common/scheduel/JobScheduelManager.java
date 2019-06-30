package org.orion.common.scheduel;

import org.orion.common.basic.AppContext;
import org.orion.common.basic.BaseBatchJob;
import org.orion.common.basic.TranscationResult;
import org.orion.common.mastercode.ErrorCode;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.StringUtil;
import org.orion.common.miscutil.Validation;
import org.orion.common.scheduel.dao.JobScheduelDao;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobScheduelManager {

    @Resource
    private Scheduler scheduler;
    @Resource
    private JobScheduelDao jobScheduelDao;
    private final Logger logger = LoggerFactory.getLogger(JobScheduelManager.class);
    private Map<String, BatchJobEntity> jobMap;

 /*   public JobScheduelManager() {
        init();
    }*/

    private void init() {
        jobMap = new HashMap<>();
        List<BatchJobEntity> batchJobList = this.getAllBatchJobs();
        if (batchJobList != null && !batchJobList.isEmpty()) {
            batchJobList.forEach((job) -> {
                jobMap.put(job.getName(), job);
            });
        }
        logger.info("BatchJob cache created. " + jobMap.size() + " records cached");
    }

    private void updateCache(String jobName, BatchJobEntity batchJob) {
        if (!StringUtil.isEmpty(jobName)) {
            if (jobMap.containsKey(jobName)) {
                if (batchJob == null)
                    jobMap.remove(jobName);
                else if(jobName.equals(batchJob.getName())) {
                    jobMap.put(jobName, batchJob);
                }
            } else {
                if (batchJob != null && jobName.equals(batchJob.getName())) {
                    jobMap.put(jobName, batchJob);
                }
            }
        }
    }

    public BatchJobEntity getBatchJob(String jobName) {
        if (!StringUtil.isEmpty(jobName)) {
            if (jobMap.containsKey(jobName)) {
                return jobMap.get(jobName);
            }
            else {
                BatchJobEntity batchJob = getBatchJobByName(jobName);
                if (batchJob != null) {
                    updateCache(jobName, batchJob);
                    logger.info("BatchJob cache updated for enquiry: " + jobName);
                    return batchJob;
                }
            }
        }
        return null;
    }

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

    public BatchJobEntity getOrionBatchJob(JobScheduel jobScheduel, boolean isQuartz, boolean isRegistered) {
        if (jobScheduel != null) {
            BatchJobEntity batchJobEntity = new BatchJobEntity();
            batchJobEntity.setName(jobScheduel.getName());
            batchJobEntity.setClassName(jobScheduel.getJobClass().getName());
            batchJobEntity.setDescription(jobScheduel.getJobDesc());
            batchJobEntity.setIsQuartz(isQuartz ? "Y" : "N");
            batchJobEntity.setIsRegistered(isRegistered ? "Y" : "N");
            return batchJobEntity;
        }
        return null;
    }

    private void registerAutoJob(JobScheduel jobScheduel) {
        registerJob(jobScheduel.getJobClass(), jobScheduel.getCron(), jobScheduel.getJobId(), jobScheduel.getGroupId(),
                jobScheduel.getTriggerId(), jobScheduel.getTriggerGroup(), jobScheduel.getJobDesc(), jobScheduel.getTriggerDesc());
    }

    @Transactional
    public TranscationResult registerAutomaticJob(JobScheduel jobScheduel, AppContext appContext, boolean needRegistion) {
        if (jobScheduel != null && appContext != null) {
            long start = System.currentTimeMillis();
            registerAutoJob(jobScheduel);
            BatchJobEntity batchJobEntity = getOrionBatchJob(jobScheduel, true, needRegistion);
            batchJobEntity.setAudit(appContext.getUserId(), DateUtil.now());
            List<ErrorCode> errorCodes = createBatchJob(batchJobEntity);
            long end = System.currentTimeMillis();
            TranscationResult result = new TranscationResult("registerAutoJobNow", start, end, errorCodes);
            return result;
        }
        return new TranscationResult();
    }

    @Transactional
    public void registerManualJob(JobScheduel jobScheduel, AppContext appContext) {
        if (jobScheduel != null && appContext != null) {
            BatchJobEntity batchJobEntity = getOrionBatchJob(jobScheduel, false, true);
            batchJobEntity.setAudit(appContext.getUserId(), DateUtil.now());
            createBatchJob(batchJobEntity);
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
            BatchJobEntity batchJobEntity = getBatchJob(jobName);
            if (batchJobEntity != null) {
                return batchJobEntity.getIsRegistered().equals("Y");
            }
        }
        return false;
    }

    public void executeJob(String jobId, boolean checkRegistion) {
        if (!StringUtil.isEmpty(jobId)) {
            try {
                BatchJobEntity batchJob = getBatchJob(jobId);
                if (batchJob != null) {
                    Class jobClass = Class.forName(batchJob.getClassName());
                    Object instance = jobClass.getConstructor().newInstance();
                    if (instance instanceof BaseBatchJob) {
                        Class baseJobClass = jobClass.getSuperclass();
                        Method method = baseJobClass.getDeclaredMethod("start");
                        method.setAccessible(true);
                        method.invoke(instance);
                    } else if(instance instanceof QuartzJobBean) {
                        Method method = jobClass.getDeclaredMethod("executeInternal");
                        method.setAccessible(true);
                        method.invoke(instance, null);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    public List<ErrorCode> createBatchJob(BatchJobEntity batchJob) {
        List<ErrorCode> errorCodes = null;
        if (batchJob != null) {
            errorCodes = Validation.doValidate(batchJob);
            if (errorCodes == null || errorCodes.isEmpty()) {
                jobScheduelDao.createBatchJob(batchJob);
                updateCache(batchJob.getName(), batchJob);
            }
        }
        return errorCodes;
    }

    public BatchJobEntity getBatchJobByName(String jobName) {
        if (!StringUtil.isEmpty(jobName))
            return jobScheduelDao.queryBatchJob(jobName);
        return null;
    }

    public List<BatchJobEntity> getAllBatchJobs() {
        return jobScheduelDao.queryAllBatchJobs();
    }

    public List<ScheduelEntity> searchJobScheduels(int page, int pageSize) {
        return jobScheduelDao.queryAllQuartzJobs(page, pageSize);
    }
}
