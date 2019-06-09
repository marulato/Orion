package org.orion.systemAdmin.batchjob;

import org.orion.common.basic.BaseBatchJob;

public class TestBatchJob3 extends BaseBatchJob {
    public TestBatchJob3(String batchjobId) {
        super("good");
    }

    @Override
    public void execute() {
        System.out.println("veryGood");
    }
}
