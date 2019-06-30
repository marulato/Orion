package org.orion.common.basic;

import org.orion.common.mastercode.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class TranscationResult {

    private boolean success;
    private List<ErrorCode> errorCodes;
    private String methodName;
    private long startTime;
    private long endTime;
    private int cost;
    private String status;

    public TranscationResult(String methodName, long startTime, long endTime, List<ErrorCode>... errorCodes) {
        if (errorCodes != null && errorCodes.length > 0) {
            this.errorCodes = new ArrayList<>();
            for (List<ErrorCode> errs : errorCodes) {
                if (errs != null)
                    this.errorCodes.addAll(errs);
            }
        }
        this.methodName = methodName;
        success = (this.errorCodes == null || this.errorCodes.isEmpty());
        this.startTime = startTime;
        this.endTime = endTime;
        cost = (int)(endTime - startTime);
        if (success)
            status = "PRO";
        else
            status = "ERR";
    }

    public TranscationResult() {
        status = "INV";
    }

    public boolean isSuccess() {
        return success;
    }

    public List<ErrorCode> getErrorCodes() {
        return errorCodes;
    }

    public int getTimeCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }

}
