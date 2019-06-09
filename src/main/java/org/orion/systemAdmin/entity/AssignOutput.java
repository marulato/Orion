package org.orion.systemAdmin.entity;

import org.orion.common.mastercode.ErrorCode;

import java.util.List;

public class AssignOutput {

    private List<ErrorCode> errorCodes;
    private List<BaseAssign> assignEntityList;

    public List<ErrorCode> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<ErrorCode> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public List<BaseAssign> getAssignEntityList() {
        return assignEntityList;
    }

    public void setAssignEntityList(List<BaseAssign> assignEntityList) {
        this.assignEntityList = assignEntityList;
    }
}
