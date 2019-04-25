package org.orion.common.basic;

public abstract class BaseBatchJob {

    private String batchjobId;
    public BaseBatchJob(String batchjobId) {

        this.batchjobId = batchjobId;
    }
    public abstract void execute();

    protected void start() {
        try {
            if (beforeExecute()) {
                execute();
            } else {

            }
        } catch (Exception e) {

        } finally {
            afterExecute();
        }
    }

    protected boolean beforeExecute() {
        return true;
    }

    protected void afterExecute() {

    }

}
