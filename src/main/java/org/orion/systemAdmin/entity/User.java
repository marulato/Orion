package org.orion.systemAdmin.entity;

import org.orion.common.audit.AuditId;
import org.orion.common.basic.BaseEntity;
import org.orion.common.validation.ValidateWithMethod;

import java.util.Date;
import java.util.Objects;

public class User extends BaseEntity {

    private Date acctActivateDt;
    private Date acctDeactivateDt;
    private String acctStatus;
    private String mobileNo;
    private String displayName;
    private String email;
    private int loginFailAttemptCnt;
    private Date loginLastAttemptDt;
    private Date loginLastSuccessDt;
    private String pwd;
    private String pwdChgRequired;
    private Date pwdLastChgDt;
    private String remarks;
    private String userDomain;
    @AuditId
    private long userId;
    @ValidateWithMethod(methodName = {"validateLoginId"}, errorCode = {""})
    @AuditId
    private String loginId;

    public User() {
        super("USER_TBL", "USER_TBL_HX");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getLoginFailAttemptCnt() == user.getLoginFailAttemptCnt() &&
                getUserId() == user.getUserId() &&
                Objects.equals(getAcctActivateDt(), user.getAcctActivateDt()) &&
                Objects.equals(getAcctDeactivateDt(), user.getAcctDeactivateDt()) &&
                Objects.equals(getAcctStatus(), user.getAcctStatus()) &&
                Objects.equals(getMobileNo(), user.getMobileNo()) &&
                Objects.equals(getDisplayName(), user.getDisplayName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getLoginLastAttemptDt(), user.getLoginLastAttemptDt()) &&
                Objects.equals(getLoginLastSuccessDt(), user.getLoginLastSuccessDt()) &&
                Objects.equals(getPwdChgRequired(), user.getPwdChgRequired()) &&
                Objects.equals(getPwdLastChgDt(), user.getPwdLastChgDt()) &&
                Objects.equals(getRemarks(), user.getRemarks()) &&
                Objects.equals(getUserDomain(), user.getUserDomain()) &&
                Objects.equals(getLoginId(), user.getLoginId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAcctActivateDt(), getAcctDeactivateDt(), getAcctStatus(), getMobileNo(),
                getDisplayName(), getEmail(), getLoginFailAttemptCnt(), getLoginLastAttemptDt(),
                getLoginLastSuccessDt(), getPwdChgRequired(), getPwdLastChgDt(), getRemarks(),
                getUserDomain(), getUserId(), getLoginId());
    }

    public Date getAcctActivateDt() {
        return acctActivateDt;
    }

    public void setAcctActivateDt(Date acctActivateDt) {
        this.acctActivateDt = acctActivateDt;
    }

    public Date getAcctDeactivateDt() {
        return acctDeactivateDt;
    }

    public void setAcctDeactivateDt(Date acctDeactivateDt) {
        this.acctDeactivateDt = acctDeactivateDt;
    }

    public String getAcctStatus() {
        return acctStatus;
    }

    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLoginFailAttemptCnt() {
        return loginFailAttemptCnt;
    }

    public void setLoginFailAttemptCnt(int loginFailAttemptCnt) {
        this.loginFailAttemptCnt = loginFailAttemptCnt;
    }

    public Date getLoginLastAttemptDt() {
        return loginLastAttemptDt;
    }

    public void setLoginLastAttemptDt(Date loginLastAttemptDt) {
        this.loginLastAttemptDt = loginLastAttemptDt;
    }

    public Date getLoginLastSuccessDt() {
        return loginLastSuccessDt;
    }

    public void setLoginLastSuccessDt(Date loginLastSuccessDt) {
        this.loginLastSuccessDt = loginLastSuccessDt;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwdChgRequired() {
        return pwdChgRequired;
    }

    public void setPwdChgRequired(String pwdChgRequired) {
        this.pwdChgRequired = pwdChgRequired;
    }

    public Date getPwdLastChgDt() {
        return pwdLastChgDt;
    }

    public void setPwdLastChgDt(Date pwdLastChgDt) {
        this.pwdLastChgDt = pwdLastChgDt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUserDomain() {
        return userDomain;
    }

    public void setUserDomain(String userDomain) {
        this.userDomain = userDomain;
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
}