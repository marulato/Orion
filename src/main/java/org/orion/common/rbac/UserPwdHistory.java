package org.orion.common.rbac;

import org.orion.common.basic.BaseEntity;

import java.util.Date;

public class UserPwdHistory extends BaseEntity {

    public UserPwdHistory() {
        super("USER_PWD_HISTORY", null);
    }

    private long userId;
    private String loginId;
    private String pwd;
    private Date pwdChgDt;
    private String pwdChgReason;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getPwdChgDt() {
        return pwdChgDt;
    }

    public void setPwdChgDt(Date pwdChgDt) {
        this.pwdChgDt = pwdChgDt;
    }

    public String getPwdChgReason() {
        return pwdChgReason;
    }

    public void setPwdChgReason(String pwdChgReason) {
        this.pwdChgReason = pwdChgReason;
    }
}
