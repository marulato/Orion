package org.orion.common.mastercode;

import org.orion.common.basic.BaseEntity;

public class ErrorCode extends BaseEntity {

    private String errorCode;
    private String errorCond;
    private String errorDesc;
    private String errorType;

    public ErrorCode() {
        super("ERROR_CODE_MC", "ERROR_CODE_MC_HX");
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCond(String errorCond) {
        this.errorCond = errorCond;
    }

    public String getErrorCond() {
        return this.errorCond;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc() {
        return this.errorDesc;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return this.errorType;
    }


}
