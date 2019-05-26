package org.orion.systemAdmin.entity;

import org.orion.common.validation.ValidateWithMethod;

import java.util.Date;

public class User {

    private Date acctActivateDt;
    private Date acctCdt;
    private Date acctDeactivateDt;
    private String acctStatus;
    private Date acctTerminateDt;
    private Date acctUdt;
    private String addr;
    private String addrCountryCode;
    private String addrPostalCode;
    private String addrState;
    private String citizenship;
    private String contactNo;
    private Date createdAt;
    private String createdByUserdomain;
    private String createdByUserid;
    private String displayName;
    private Date dob;
    private String email;
    private String firstName;
    private String gender;
    private String identityNo;
    private String isFirstTimeLogin;
    private String isSystem;
    private String lastName;
    private Date loginnullLastSuccessDt;
    private int loginFailAttemptCnt;
    private Date loginLastAttemptDt;
    private Date loginLastSuccessDt;
    private String maritalStatus;
    private String middleName;
    private String mobileNo;
    private String passportNo;
    private String pwd;
    private String pwdChallengeAns;
    private String pwdChallengeHint;
    private String pwdChallengeQn;
    private String pwdChgRequired;
    private Date pwdLastChgDt;
    private String reason;
    private String remarks;
    private String salutation;
    private Date updatedAt;
    private String updatedByUserdomain;
    private String updatedByUserid;
    private String userDomain;
    @ValidateWithMethod(methodName = {""}, errorCode = {"001"})
    private String userId;

    public void setAcctActivateDt(Date acctActivateDt) {
        this.acctActivateDt = acctActivateDt;
    }

    public Date getAcctActivateDt() {
        return this.acctActivateDt;
    }

    public void setAcctCdt(Date acctCdt) {
        this.acctCdt = acctCdt;
    }

    public Date getAcctCdt() {
        return this.acctCdt;
    }

    public void setAcctDeactivateDt(Date acctDeactivateDt) {
        this.acctDeactivateDt = acctDeactivateDt;
    }

    public Date getAcctDeactivateDt() {
        return this.acctDeactivateDt;
    }

    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    public String getAcctStatus() {
        return this.acctStatus;
    }

    public void setAcctTerminateDt(Date acctTerminateDt) {
        this.acctTerminateDt = acctTerminateDt;
    }

    public Date getAcctTerminateDt() {
        return this.acctTerminateDt;
    }

    public void setAcctUdt(Date acctUdt) {
        this.acctUdt = acctUdt;
    }

    public Date getAcctUdt() {
        return this.acctUdt;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setAddrCountryCode(String addrCountryCode) {
        this.addrCountryCode = addrCountryCode;
    }

    public String getAddrCountryCode() {
        return this.addrCountryCode;
    }

    public void setAddrPostalCode(String addrPostalCode) {
        this.addrPostalCode = addrPostalCode;
    }

    public String getAddrPostalCode() {
        return this.addrPostalCode;
    }

    public void setAddrState(String addrState) {
        this.addrState = addrState;
    }

    public String getAddrState() {
        return this.addrState;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getCitizenship() {
        return this.citizenship;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedByUserdomain(String createdByUserdomain) {
        this.createdByUserdomain = createdByUserdomain;
    }

    public String getCreatedByUserdomain() {
        return this.createdByUserdomain;
    }

    public void setCreatedByUserid(String createdByUserid) {
        this.createdByUserid = createdByUserid;
    }

    public String getCreatedByUserid() {
        return this.createdByUserid;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDob() {
        return this.dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getIdentityNo() {
        return this.identityNo;
    }

    public void setIsFirstTimeLogin(String isFirstTimeLogin) {
        this.isFirstTimeLogin = isFirstTimeLogin;
    }

    public String getIsFirstTimeLogin() {
        return this.isFirstTimeLogin;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getIsSystem() {
        return this.isSystem;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLoginnullLastSuccessDt(Date loginnullLastSuccessDt) {
        this.loginnullLastSuccessDt = loginnullLastSuccessDt;
    }

    public Date getLoginnullLastSuccessDt() {
        return this.loginnullLastSuccessDt;
    }

    public void setLoginFailAttemptCnt(int loginFailAttemptCnt) {
        this.loginFailAttemptCnt = loginFailAttemptCnt;
    }

    public int getLoginFailAttemptCnt() {
        return this.loginFailAttemptCnt;
    }

    public void setLoginLastAttemptDt(Date loginLastAttemptDt) {
        this.loginLastAttemptDt = loginLastAttemptDt;
    }

    public Date getLoginLastAttemptDt() {
        return this.loginLastAttemptDt;
    }

    public void setLoginLastSuccessDt(Date loginLastSuccessDt) {
        this.loginLastSuccessDt = loginLastSuccessDt;
    }

    public Date getLoginLastSuccessDt() {
        return this.loginLastSuccessDt;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() {
        return this.maritalStatus;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportNo() {
        return this.passportNo;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwdChallengeAns(String pwdChallengeAns) {
        this.pwdChallengeAns = pwdChallengeAns;
    }

    public String getPwdChallengeAns() {
        return this.pwdChallengeAns;
    }

    public void setPwdChallengeHint(String pwdChallengeHint) {
        this.pwdChallengeHint = pwdChallengeHint;
    }

    public String getPwdChallengeHint() {
        return this.pwdChallengeHint;
    }

    public void setPwdChallengeQn(String pwdChallengeQn) {
        this.pwdChallengeQn = pwdChallengeQn;
    }

    public String getPwdChallengeQn() {
        return this.pwdChallengeQn;
    }

    public void setPwdChgRequired(String pwdChgRequired) {
        this.pwdChgRequired = pwdChgRequired;
    }

    public String getPwdChgRequired() {
        return this.pwdChgRequired;
    }

    public void setPwdLastChgDt(Date pwdLastChgDt) {
        this.pwdLastChgDt = pwdLastChgDt;
    }

    public Date getPwdLastChgDt() {
        return this.pwdLastChgDt;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getSalutation() {
        return this.salutation;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedByUserdomain(String updatedByUserdomain) {
        this.updatedByUserdomain = updatedByUserdomain;
    }

    public String getUpdatedByUserdomain() {
        return this.updatedByUserdomain;
    }

    public void setUpdatedByUserid(String updatedByUserid) {
        this.updatedByUserid = updatedByUserid;
    }

    public String getUpdatedByUserid() {
        return this.updatedByUserid;
    }

    public void setUserDomain(String userDomain) {
        this.userDomain = userDomain;
    }

    public String getUserDomain() {
        return this.userDomain;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

}