package org.orion.systemAdmin.batchjob;

import org.orion.common.basic.AppContext;
import org.orion.common.basic.BaseBatchJob;

public class TestBatchJob extends BaseBatchJob {

    public TestBatchJob(String jobName, String jobGroup, AppContext context) {
        super(jobName, jobGroup, context);
    }

    @Override
    public void execute() {
        System.out.println("Test Maunal Run");
    }
}
