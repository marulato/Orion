package org.orion.common.basic;

import org.orion.common.miscutil.DateUtil;
import org.orion.common.scheduel.RegisterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseBatchJob {

    private String batchjobId;
    private Exception exception;
    private final Logger logger = LoggerFactory.getLogger(BaseBatchJob.class);
    public BaseBatchJob(String batchjobId) {

        this.batchjobId = batchjobId;
    }
    public abstract void execute();

    protected void start() {
        long startTime = System.currentTimeMillis();
        logger.info(batchjobId + " Started at " + DateUtil.getStandardDate(DateUtil.getDate(startTime)));
        try {
            if (beforeExecute()) {
                execute();
            } else {
                logger.warn("The Batch Job [" + batchjobId + "] is not registered in the system, execution terminated.");
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            afterExecute();
            updateBatchJobStatus();
            long endTime = System.currentTimeMillis();
            logger.info(batchjobId + " Completed at " + DateUtil.getStandardDate(DateUtil.getDate(endTime)));
            logger.info("Time costs: " + (endTime - startTime) + " ms");
        }
    }

    protected boolean beforeExecute() {
        return RegisterManager.isRegistered(batchjobId);
    }

    protected void afterExecute() {
        if (exception != null) {
            logger.error("BatchJob [" + batchjobId + "] terminated because of an exception", exception);
        }
    }

    private void updateBatchJobStatus() {

    }

}
