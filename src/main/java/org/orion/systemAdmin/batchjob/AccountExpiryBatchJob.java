package org.orion.systemAdmin.batchjob;

import org.orion.common.basic.AppContext;
import org.orion.common.miscutil.SpringUtil;
import org.orion.common.schedule.JobScheduleManager;
import org.orion.common.schedule.entity.BatchJobFiredHistory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class AccountExpiryBatchJob extends QuartzJobBean {
    private boolean isAutomatic = true;
    private AppContext context;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobScheduleManager jobScheduleManager = SpringUtil.getBean(JobScheduleManager.class);
        BatchJobFiredHistory firedHistory = jobScheduleManager.prepareQrtzJobFireHistory(AccountExpiryBatchJob.class);
        execute();
        if (isAutomatic) {
            jobScheduleManager.createQrtzFinalJobFireHistory(firedHistory);
        } else {
            jobScheduleManager.createQrtzFinalJobFireHistoryByManual(firedHistory, context);
        }
    }

    private void execute() {
        System.out.println(new Date());
    }

}
