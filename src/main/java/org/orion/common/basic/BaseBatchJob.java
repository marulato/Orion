package org.orion.common.basic;

import org.orion.common.dao.crud.CrudManager;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.SpringUtil;
import org.orion.common.schedule.JobScheduleManager;
import org.orion.common.schedule.entity.BatchJobEntity;
import org.orion.common.schedule.entity.BatchJobFiredHistory;
import org.orion.systemAdmin.entity.AppConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class BaseBatchJob {

    private String jobName;
    private String jobGroup;
    private AppContext context;
    private Exception exception;
    private BatchJobFiredHistory firedHistory;
    private final Logger logger = LoggerFactory.getLogger(BaseBatchJob.class);
    public BaseBatchJob(String jobName, String jobGroup, AppContext context) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.context = context;
    }
    public abstract void execute();

    protected void start() {
        long startTime = System.currentTimeMillis();
        logger.info(jobName + " Started at " + DateUtil.getStandardDate(DateUtil.getDate(startTime)));
        try {
            prepareBatchJobHistory();
            if (beforeExecute()) {
                execute();
                firedHistory.setStatus(AppConsts.BATCHJOB_STATUS_COMPLETED);
            } else {
                firedHistory.setStatus(AppConsts.BATCHJOB_STATUS_CANCELED);
                logger.warn("The Batch Job [" + jobName + "] is not registered in the system, execution terminated.");
            }
        } catch (Exception e) {
            firedHistory.setStatus(AppConsts.BATCHJOB_STATUS_EXCEPTION);
            exception = e;
        } finally {
            afterExecute();
            long endTime = System.currentTimeMillis();
            firedHistory.setStopTime(endTime);
            firedHistory.setTimeCost(endTime - firedHistory.getFireTime());
            firedHistory.setUpdateAudit(context.getUser().getLoginId(), new Date());
            CrudManager crudManager = SpringUtil.getBean(CrudManager.class);
            crudManager.update(firedHistory);
            logger.info(jobName + " Completed at " + DateUtil.getStandardDate(DateUtil.getDate(endTime)));
            logger.info("Time costs: " + (endTime - startTime) + " ms");
        }
    }

    protected boolean beforeExecute() {
        if (context != null) {
            return true;
        }
        return false;
    }

    protected void afterExecute() {
        if (exception != null) {
            logger.error("BatchJob [" + jobName + "] terminated because of an exception", exception);
        }
    }

    private void prepareBatchJobHistory() {
        JobScheduleManager jobScheduleManager = SpringUtil.getBean(JobScheduleManager.class);
        BatchJobEntity jobEntity = jobScheduleManager.getBatchJobByNameAndGroup(jobName, jobGroup);
        if (jobEntity != null) {
            Date now = new Date();
            BatchJobFiredHistory jobFiredHistory = new BatchJobFiredHistory();
            this.firedHistory = jobFiredHistory;
            jobFiredHistory.setJobName(jobEntity.getJobName());
            jobFiredHistory.setJobGroup(jobEntity.getJobGroup());
            jobFiredHistory.setFireTime(System.currentTimeMillis());
            jobFiredHistory.setStopTime(System.currentTimeMillis());
            jobFiredHistory.setTimeCost(0);
            jobFiredHistory.setStatus(AppConsts.BATCHJOB_STATUS_RUNNING);
            jobFiredHistory.setFireMethod(AppConsts.BATCHJOB_FIRE_TYPE_MANUAL);
            firedHistory.setAudit(context.getUser().getLoginId(), new Date());
            jobScheduleManager.createJobHistory(jobFiredHistory);
        }
    }
}
