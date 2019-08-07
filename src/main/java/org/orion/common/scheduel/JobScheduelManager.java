package org.orion.common.scheduel;

import org.orion.common.basic.AppContext;
import org.orion.common.basic.BaseBatchJob;
import org.orion.common.basic.SearchParam;
import org.orion.common.dao.crud.CrudManager;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.StringUtil;
import org.orion.common.scheduel.dao.JobScheduelDao;
import org.orion.common.scheduel.entity.BatchJobEntity;
import org.orion.common.scheduel.entity.BatchJobFiredHistory;
import org.orion.common.scheduel.entity.JobScheduel;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JobScheduelManager {

    @Resource
    private Scheduler scheduler;
    @Resource
    private JobScheduelDao jobScheduelDao;
    @Resource
    private CrudManager crudManager;
    private final Logger logger = LoggerFactory.getLogger(JobScheduelManager.class);
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

    public BatchJobEntity initOrionBatchJob(JobScheduel jobScheduel) {
        if (jobScheduel != null) {
            BatchJobEntity batchJobEntity = new BatchJobEntity();
            batchJobEntity.setJobName(jobScheduel.getJobName());
            batchJobEntity.setJobGroup(jobScheduel.getJobGroup());
            batchJobEntity.setClassName(jobScheduel.getClassName());
            batchJobEntity.setDescription(jobScheduel.getJobDesc());
            batchJobEntity.setIsQuartz(jobScheduel.getAutomatic());
            batchJobEntity.setIsRegistered(jobScheduel.getRegister());
            return batchJobEntity;
        }
        return null;
    }

    public JobScheduel getJobScheduelFromBatchJob(BatchJobEntity jobEntity) {
        if (jobEntity != null) {
            JobScheduel jobScheduel = new JobScheduel();
            jobScheduel.setJobName(jobEntity.getJobName());
            jobScheduel.setJobGroup(jobEntity.getJobGroup());
            jobScheduel.setJobDesc(jobEntity.getDescription());
            jobScheduel.setClassName(jobEntity.getClassName());
            jobScheduel.setCron(jobEntity.getCron());
            jobScheduel.setAutomatic(jobEntity.getIsQuartz());
            jobScheduel.setRegister(jobEntity.getIsRegistered());
            return jobScheduel;
        }
        return null;
    }

    private void registerAutoJob(JobScheduel jobScheduel) throws Exception {
        if (StringUtil.isEmpty(jobScheduel.getTriggerGroup())) {
            jobScheduel.setTriggerGroup(AppConsts.DEFAULT_TRIGGER_GROUP);
        }
        if (StringUtil.isEmpty(jobScheduel.getTriggerName())) {
            jobScheduel.setTriggerName(AppConsts.DEFAULT_TRIGGER_NAME + "_" + jobScheduel.getJobName());
        }
        if (StringUtil.isEmpty(jobScheduel.getTriggerDesc())) {
            jobScheduel.setTriggerDesc("Orion Default Trigger");
        }
        registerJob(jobScheduel.getJobClass(), jobScheduel.getCron(), jobScheduel.getJobName(), jobScheduel.getJobGroup(),
                jobScheduel.getTriggerName(), jobScheduel.getTriggerGroup(), jobScheduel.getJobDesc(), jobScheduel.getTriggerDesc());
    }

    @Transactional
    public void registerQuartzJob(JobScheduel jobScheduel, AppContext appContext) throws Exception {
        if (jobScheduel != null && appContext != null) {
            registerAutoJob(jobScheduel);
            BatchJobEntity batchJobEntity = initOrionBatchJob(jobScheduel);
            batchJobEntity.setAudit(appContext.getUser().getLoginId(), DateUtil.now());
            batchJobEntity.setCron(jobScheduel.getCron());
            createBatchJob(batchJobEntity);
        }
    }

    @Transactional
    public void registerManualJob(JobScheduel jobScheduel, AppContext appContext) {
        if (jobScheduel != null && appContext != null) {
            BatchJobEntity batchJobEntity = initOrionBatchJob(jobScheduel);
            batchJobEntity.setAudit(appContext.getUser().getLoginId(), DateUtil.now());
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

    @Transactional
    public void deleteQuartzJob(JobScheduel jobScheduel) throws Exception {
        if (jobScheduel == null) {
            logger.warn("");
            return;
        }
        TriggerKey triggerKey = TriggerKey.triggerKey(jobScheduel.getTriggerName(), jobScheduel.getTriggerGroup());
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        JobKey jobKey = JobKey.jobKey(jobScheduel.getJobName(), jobScheduel.getJobGroup());
        scheduler.deleteJob(jobKey);
        BatchJobEntity batchJobEntity = initOrionBatchJob(jobScheduel);
        crudManager.delete(batchJobEntity);
    }

    public void deleteManualJob(JobScheduel jobScheduel) throws Exception {
        if (jobScheduel == null) {
            logger.warn("");
            return;
        }
        BatchJobEntity batchJobEntity = initOrionBatchJob(jobScheduel);
        crudManager.delete(batchJobEntity);
    }

    public void modifyQuartzJob(JobScheduel jobScheduel) {
        if (jobScheduel != null) {
            try {
                TriggerKey oldTriggerKey = TriggerKey.triggerKey(jobScheduel.getJobName(), jobScheduel.getJobGroup());
                if (oldTriggerKey == null) {
                    oldTriggerKey = TriggerKey.triggerKey(jobScheduel.getTriggerName(), jobScheduel.getTriggerGroup());
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

    public void executeJob(JobScheduel jobScheduel, boolean checkRegistion) {
        if (jobScheduel != null) {
            try {
                BatchJobEntity batchJob = getBatchJobByNameAndGroup(jobScheduel.getJobName(), jobScheduel.getJobGroup());
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
                        method.invoke(instance);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    public void createBatchJob(BatchJobEntity batchJob) {
        if (batchJob != null) {
            crudManager.create(batchJob);
        }
    }

    public BatchJobEntity getBatchJobByNameAndGroup(String jobName, String jobGroup) {
        if (!StringUtil.isEmpty(jobName) && !StringUtil.isEmpty(jobGroup))
            return jobScheduelDao.queryBatchJob(jobName, jobGroup);
        return null;
    }

    public List<BatchJobEntity> getAllBatchJobs() {
        return jobScheduelDao.queryAllBatchJobs();
    }

    public void updateMaunalJob(BatchJobEntity batchJobEntity) {
        if (batchJobEntity != null) {
            crudManager.update(batchJobEntity);
        }
    }

    public List<BatchJobEntity> search(SearchParam<BatchJobEntity> searchParam) {
        if (searchParam != null && searchParam.getObject() != null) {
            return jobScheduelDao.search(searchParam);
        }
        return new ArrayList<>();
    }

    public int getTotalPages(SearchParam<BatchJobEntity> searchParam) {
        if (searchParam != null && searchParam.getObject() != null) {
            return jobScheduelDao.getCountsBySearchParam(searchParam);
        }
        return -1;
    }

    public BatchJobEntity getBatchJobByTrigger(JobScheduel jobScheduel) {
        if (jobScheduel != null) {
            return jobScheduelDao.queryBatchJobByTrigger(jobScheduel);
        }
        return null;
    }

    public BatchJobFiredHistory getLatestFired(BatchJobEntity jobEntity) {
        if (jobEntity != null) {
            List<BatchJobFiredHistory> jobFiredHistories = jobScheduelDao.getJobFireHistory(jobEntity);
            if (!jobFiredHistories.isEmpty()) {
                return jobFiredHistories.get(0);
            }
        }
        return null;
    }

}
