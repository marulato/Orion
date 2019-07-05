package org.orion.systemAdmin.entity;

import java.util.Date;

public class UserLoginHistory {
    private long userId;
    private String loginId;
    private Date loginTime;
    private String isSuccess;
    private String client;
    private String sessionId;
    private String reason;

    public UserLoginHistory(User user) {
        if (user != null) {
            this.userId = user.getUserId();
            this.loginId = user.getLoginId();
            this.loginTime = user.getLoginLastAttemptDt();
        }
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess ? "Y" : "N";
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

