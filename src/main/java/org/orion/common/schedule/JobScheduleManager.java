package org.orion.common.schedule;

import org.orion.common.audit.AuditTrail;
import org.orion.common.basic.AppContext;
import org.orion.common.basic.SearchParam;
import org.orion.common.dao.crud.CrudManager;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.StringUtil;
import org.orion.common.schedule.dao.JobScheduleDao;
import org.orion.common.schedule.entity.*;
import org.orion.systemAdmin.entity.AppConsts;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JobScheduleManager {

    @Resource
    private Scheduler scheduler;
    @Resource
    private JobScheduleDao jobScheduleDao;
    @Resource
    private CrudManager crudManager;
    private final Logger logger = LoggerFactory.getLogger(JobScheduleManager.class);
    private Map<String, BatchJobEntity> jobMap;

    private void registerJob(Class<? extends QuartzJobBean> batchJobClass, String cron, String jobName, String jobGroup,
                          String triggerName, String triggerGroup, String jobDesc, String triggerDesc) {
        if (batchJobClass == null || StringUtil.isEmpty(cron) || StringUtil.isEmpty(jobName) || StringUtil.isEmpty(jobGroup)) {
            logger.warn("");
            return;
        }
        if (StringUtil.isEmpty(triggerName))
            triggerName = jobName;
        if (StringUtil.isEmpty(triggerGroup))
            triggerGroup = jobGroup;
        try {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
            CronTrigger triggerOld = null;
            triggerOld = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (triggerOld != null) {
                logger.warn("Batch Job already EXSITS");
                return;
            } else {
                JobDetail jobDetail = JobBuilder.newJob(batchJobClass).withIdentity(jobName, jobGroup).build();
                JobDetailImpl finalJob = (JobDetailImpl) jobDetail;
                finalJob.setDescription(jobDesc);
                CronTrigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity(triggerName, triggerGroup).
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

    public BatchJobEntity initOrionBatchJob(JobSchedule jobSchedule) {
        if (jobSchedule != null) {
            BatchJobEntity batchJobEntity = new BatchJobEntity();
            batchJobEntity.setJobName(jobSchedule.getJobName());
            batchJobEntity.setJobGroup(jobSchedule.getJobGroup());
            batchJobEntity.setClassName(jobSchedule.getClassName());
            batchJobEntity.setCron(jobSchedule.getCron());
            batchJobEntity.setDescription(jobSchedule.getJobDesc());
            batchJobEntity.setIsQuartz(jobSchedule.getAutomatic());
            batchJobEntity.setIsRegistered(jobSchedule.getRegister());
            return batchJobEntity;
        }
        return null;
    }

    public BatchJobEntity initOrionBatchJob(JobSchedule jobSchedule, BatchJobEntity batchJobEntity) {
        BatchJobEntity jobEntity = initOrionBatchJob(jobSchedule);
        if (jobEntity != null && batchJobEntity != null) {
            jobEntity.setCreatedAt(batchJobEntity.getCreatedAt());
            jobEntity.setCreatedBy(batchJobEntity.getCreatedBy());
            jobEntity.setUpdatedAt(batchJobEntity.getUpdatedAt());
            jobEntity.setUpdatedBy(batchJobEntity.getUpdatedBy());
        }
        return jobEntity;
    }

    public JobSchedule getJobScheduleFromBatchJob(BatchJobEntity jobEntity) {
        if (jobEntity != null) {
            JobSchedule jobSchedule = new JobSchedule();
            jobSchedule.setJobName(jobEntity.getJobName());
            jobSchedule.setJobGroup(jobEntity.getJobGroup());
            jobSchedule.setJobDesc(jobEntity.getDescription());
            jobSchedule.setClassName(jobEntity.getClassName());
            jobSchedule.setCron(jobEntity.getCron());
            jobSchedule.setAutomatic(jobEntity.getIsQuartz());
            jobSchedule.setRegister(jobEntity.getIsRegistered());
            jobSchedule.setCreatedAt(jobEntity.getCreatedAt());
            jobSchedule.setCreatedBy(jobEntity.getCreatedBy());
            jobSchedule.setUpdatedAt(jobEntity.getUpdatedAt());
            jobSchedule.setUpdatedBy(jobEntity.getUpdatedBy());
            return jobSchedule;
        }
        return null;
    }

    private void registerAutoJob(JobSchedule jobSchedule) throws Exception {
        if (StringUtil.isEmpty(jobSchedule.getTriggerGroup())) {
            jobSchedule.setTriggerGroup(AppConsts.DEFAULT_TRIGGER_GROUP);
        }
        if (StringUtil.isEmpty(jobSchedule.getTriggerName())) {
            jobSchedule.setTriggerName(AppConsts.DEFAULT_TRIGGER_NAME + "_" + jobSchedule.getJobName() +
                    "_" + System.currentTimeMillis());
        }
        if (StringUtil.isEmpty(jobSchedule.getTriggerDesc())) {
            jobSchedule.setTriggerDesc("Orion Default Trigger");
        }
        registerJob(jobSchedule.getJobClass(), jobSchedule.getCron(), jobSchedule.getJobName(), jobSchedule.getJobGroup(),
                jobSchedule.getTriggerName(), jobSchedule.getTriggerGroup(), jobSchedule.getJobDesc(), jobSchedule.getTriggerDesc());
    }

    @Transactional
    public void registerQuartzJob(JobSchedule jobSchedule, AppContext appContext) throws Exception {
        if (jobSchedule != null && appContext != null) {
            registerAutoJob(jobSchedule);
            BatchJobEntity batchJobEntity = initOrionBatchJob(jobSchedule);
            batchJobEntity.setAudit(appContext.getUser().getLoginId(), DateUtil.now());
            batchJobEntity.setCron(jobSchedule.getCron());
            createBatchJob(batchJobEntity);
            AuditTrail<BatchJobEntity> afterInsert = new AuditTrail<>(batchJobEntity, AppConsts.AUDIT_AFTER_INSERT, appContext);
            crudManager.createAudit(afterInsert);
        }
    }

    @Transactional
    public void registerManualJob(JobSchedule jobSchedule, AppContext appContext) {
        if (jobSchedule != null && appContext != null) {
            BatchJobEntity batchJobEntity = initOrionBatchJob(jobSchedule);
            batchJobEntity.setAudit(appContext.getUser().getLoginId(), DateUtil.now());
            batchJobEntity.setCron(null);
            createBatchJob(batchJobEntity);
            AuditTrail<BatchJobEntity> afterInsert = new AuditTrail<>(batchJobEntity, AppConsts.AUDIT_AFTER_INSERT, appContext);
            crudManager.createAudit(afterInsert);
        }
    }

    public void startNow() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("计划任务启动失败", e);
        }
    }

    public void unsheduelQuartzJob(QrtzTriggers triggers) throws Exception {
        if (triggers != null) {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggers.getTriggerName(), triggers.getTriggerGroup());
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            JobKey jobKey = JobKey.jobKey(triggers.getJobName(), triggers.getJobGroup());
            scheduler.deleteJob(jobKey);
        }
    }

    @Transactional
    public void deleteQuartzJob(String jobName, String jobGroup, AppContext context) throws Exception {
        BatchJobEntity batchJobEntity = getBatchJobByNameAndGroup(jobName, jobGroup);
        unsheduelQuartzJob(getQrtzTrigger(jobName, jobGroup));
        AuditTrail<BatchJobEntity> beforeDelete = new AuditTrail<>(batchJobEntity, AppConsts.AUDIT_BEFOR_DELETE, context);
        crudManager.delete(batchJobEntity);
        crudManager.createAudit(beforeDelete);
    }

    public void deleteManualJob(String jobName, String jobGroup, AppContext context) throws Exception {
        BatchJobEntity batchJobEntity = getBatchJobByNameAndGroup(jobName, jobGroup);
        AuditTrail<BatchJobEntity> beforeDelete = new AuditTrail<>(batchJobEntity, AppConsts.AUDIT_BEFOR_DELETE, context);
        crudManager.delete(batchJobEntity);
        crudManager.createAudit(beforeDelete);
    }

    public void modifyQuartzJob(JobSchedule jobSchedule) {
        if (jobSchedule != null) {
            QrtzCronTriggers cronTriggers = getCronTriggerByJob(jobSchedule.getJobName(), jobSchedule.getJobGroup());
            try {
                TriggerKey oldTriggerKey = TriggerKey.triggerKey(cronTriggers.getTriggerName(), cronTriggers.getTriggerGroup());
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobSchedule.getCron());
                CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                        .withIdentity(oldTriggerKey).withSchedule(scheduleBuilder).build();
                scheduler.rescheduleJob(oldTriggerKey, cronTrigger);

            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    public void pauseJob(String jobName, String groupId) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, groupId);
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public void resumeJob(String jobName, String groupId) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, groupId);
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

    public boolean isRegistered(String jobName, String jobGroup) {
        if (!StringUtil.isEmpty(jobName)) {
            BatchJobEntity batchJobEntity = getBatchJobByNameAndGroup(jobName, jobGroup);
            if (batchJobEntity != null) {
                return batchJobEntity.getIsRegistered().equals("Y");
            }
        }
        return false;
    }

    @Transactional
    public void executeJob(BatchJobEntity jobEntity, AppContext context) throws Exception {
        if (jobEntity != null && context != null) {
            BatchJobEntity batchJob = getBatchJobByNameAndGroup(jobEntity.getJobName(), jobEntity.getJobGroup());
            if (batchJob != null) {
                Class jobClass = Class.forName(batchJob.getClassName());
                if (AppConsts.YES.equals(batchJob.getIsQuartz())) {
                    Object qrtzInstance = jobClass.getConstructor().newInstance();
                    Field contextField = jobClass.getDeclaredField("context");
                    Field automaticField = jobClass.getDeclaredField("isAutomatic");
                    automaticField.setAccessible(true);
                    contextField.setAccessible(true);
                    contextField.set(qrtzInstance, context);
                    automaticField.set(qrtzInstance, false);
                    Method method = jobClass.getDeclaredMethod("executeInternal", JobExecutionContext.class);
                    method.setAccessible(true);
                    method.invoke(qrtzInstance, new Object[1]);
                } else {
                    Object manualInstance = jobClass.getConstructor(String.class, String.class, AppContext.class).
                            newInstance(batchJob.getJobName(), batchJob.getJobGroup(), context);
                    Class baseJobClass = jobClass.getSuperclass();
                    Method method = baseJobClass.getDeclaredMethod("start");
                    method.setAccessible(true);
                    method.invoke(manualInstance);
                }
            }
        }
    }

    public BatchJobFiredHistory prepareQrtzJobFireHistory(Class<?> jobClass) {
        BatchJobEntity jobEntity = getQrtzJobByClass(jobClass.getName());
        if (jobEntity != null) {
            Date now = new Date();
            BatchJobFiredHistory jobFiredHistory = new BatchJobFiredHistory();
            jobFiredHistory.setJobName(jobEntity.getJobName());
            jobFiredHistory.setJobGroup(jobEntity.getJobGroup());
            jobFiredHistory.setFireTime(System.currentTimeMillis());
            jobFiredHistory.setStopTime(System.currentTimeMillis());
            jobFiredHistory.setTimeCost(0);
            jobFiredHistory.setStatus(AppConsts.BATCHJOB_STATUS_RUNNING);
            jobFiredHistory.setFireMethod(AppConsts.BATCHJOB_FIRE_TYPE_AUTO);
            jobFiredHistory.setAudit("Automatic Scheduling System", now);
            createJobHistory(jobFiredHistory);
            return jobFiredHistory;
        }
        return null;
    }

    public void createQrtzFinalJobFireHistory(BatchJobFiredHistory firedHistory) {
        if (firedHistory != null) {
            firedHistory.setStopTime(System.currentTimeMillis());
            firedHistory.setUpdatedAt(new Date());
            firedHistory.setTimeCost(firedHistory.getStopTime() - firedHistory.getFireTime());
            firedHistory.setStatus(AppConsts.BATCHJOB_STATUS_COMPLETED);
            crudManager.update(firedHistory);
        }
    }

    public void createQrtzFinalJobFireHistoryByManual(BatchJobFiredHistory firedHistory, AppContext context) {
        if (firedHistory != null && context != null) {
            firedHistory.setStopTime(System.currentTimeMillis());
            firedHistory.setUpdateAudit(context.getUser().getLoginId(), new Date());
            firedHistory.setCreatedBy(context.getUser().getLoginId());
            firedHistory.setTimeCost(firedHistory.getStopTime() - firedHistory.getFireTime());
            firedHistory.setFireMethod(AppConsts.BATCHJOB_FIRE_TYPE_MANUAL);
            firedHistory.setStatus(AppConsts.BATCHJOB_STATUS_COMPLETED);
            crudManager.update(firedHistory);
        }
    }

    public void createBatchJob(BatchJobEntity batchJob) {
        if (batchJob != null) {
            crudManager.create(batchJob);
        }
    }

    public BatchJobEntity getBatchJobByNameAndGroup(String jobName, String jobGroup) {
        if (!StringUtil.isEmpty(jobName) && !StringUtil.isEmpty(jobGroup))
            return jobScheduleDao.queryBatchJobByNameAndGroup(jobName, jobGroup);
        return null;
    }

    public BatchJobEntity getBatchJobBySchedule(JobSchedule jobSchedule) {
        if (jobSchedule != null) {
            return jobScheduleDao.queryBatchJobBySchedule(jobSchedule);
        }
        return null;
    }

    public List<BatchJobEntity> getAllBatchJobs() {
        return jobScheduleDao.queryAllBatchJobs();
    }

    @Transactional
    public void updateMaunalJob(BatchJobEntity batchJobEntity, AppContext appContext) {
        if (batchJobEntity != null && appContext != null) {
            if (AppConsts.NO.equals(batchJobEntity.getIsQuartz())) {
                batchJobEntity.setCron(null);
            }
            batchJobEntity.setUpdateAudit(appContext.getUser().getLoginId(), DateUtil.now());
            BatchJobEntity beforeModify = getBatchJobByNameAndGroup(batchJobEntity.getJobName(), batchJobEntity.getJobGroup());
            AuditTrail<BatchJobEntity> before = new AuditTrail<>(beforeModify, AppConsts.AUDIT_BEFOR_MODIFY, appContext);
            crudManager.createAudit(before);
            crudManager.update(batchJobEntity);
            AuditTrail<BatchJobEntity> after = new AuditTrail<>(batchJobEntity, AppConsts.AUDIT_AFTER_MODIFY, appContext);
            crudManager.createAudit(after);
        }
    }

    public List<BatchJobEntity> search(SearchParam<BatchJobEntity> searchParam) {
        if (searchParam != null && searchParam.getObject() != null) {
            return jobScheduleDao.search(searchParam);
        }
        return new ArrayList<>();
    }

    public int getTotalPages(SearchParam<BatchJobEntity> searchParam) {
        if (searchParam != null && searchParam.getObject() != null) {
            int rows = jobScheduleDao.getCountsBySearchParam(searchParam);
            int pageSize = searchParam.getPageSize();
            if (rows % pageSize == 0) {
                return rows / pageSize;
            } else {
                return rows / pageSize + 1;
            }
        }
        return 0;
    }

    public QrtzCronTriggers getCronTriggerByJob(String jobName, String jobGroup) {
        QrtzTriggers trigger = getQrtzTrigger(jobName, jobGroup);
        if (trigger != null) {
            QrtzCronTriggers cronTrigger = getCronTrigger(trigger.getTriggerName(), trigger.getTriggerGroup());
            return cronTrigger;
        }
        return null;
    }

    public BatchJobEntity getBatchJobByTrigger(JobSchedule jobSchedule) {
        if (jobSchedule != null) {
            return jobScheduleDao.queryBatchJobByTrigger(jobSchedule);
        }
        return null;
    }

    public BatchJobFiredHistory getLatestFired(BatchJobEntity jobEntity) {
        if (jobEntity != null) {
            List<BatchJobFiredHistory> jobFiredHistories = jobScheduleDao.getJobFireHistory(jobEntity);
            if (!jobFiredHistories.isEmpty()) {
                return jobFiredHistories.get(0);
            }
        }
        return null;
    }

    public QrtzJobDetails getQrtzJobDetails(String jobName, String jobGroup) {
        if (!StringUtil.isEmpty(jobName) && !StringUtil.isEmpty(jobGroup)) {
            return jobScheduleDao.queryQrtzJobDetailsNameAndGroup(jobName, jobGroup);
        }
        return null;
    }

    public QrtzTriggers getQrtzTrigger(String jobName, String jobGroup) {
        if (!StringUtil.isEmpty(jobGroup) && !StringUtil.isEmpty(jobGroup)) {
            return jobScheduleDao.queryTrigger(jobName, jobGroup);
        }
        return null;
    }

    public QrtzCronTriggers getCronTrigger(String triggerName, String triggerGroup) {
        if (!StringUtil.isEmpty(triggerName) && !StringUtil.isEmpty(triggerGroup)) {
            return jobScheduleDao.queryCronTrigger(triggerName, triggerGroup);
        }
        return null;
    }

    public BatchJobEntity getQrtzJobByClass(String className) {
        return jobScheduleDao.queryQrtzJobByClass(className);
    }

    public void createJobHistory(BatchJobFiredHistory firedHistory) {
        if (firedHistory != null) {
            jobScheduleDao.insertJobHistory(firedHistory);
        }
    }

    public BatchJobFiredHistory getRunningJobHistory(String jobName, String jobGroup) {
        return jobScheduleDao.queryRunningJobHistory(jobName, jobGroup);
    }

    public List<BatchJobParam> getJobParams(String jobName, String jobGroup) {
        return jobScheduleDao.queryJobParam(jobName, jobGroup);
    }

    public void deleteJobParams(String jobName, String jobGroup) {
        jobScheduleDao.deleteAllJobParams(jobName, jobGroup);
    }

    public void addJobParams(List<BatchJobParam> jobParams) {
        if (jobParams != null && !jobParams.isEmpty()) {
            jobScheduleDao.insertJobParams(jobParams);
        }
    }

}
