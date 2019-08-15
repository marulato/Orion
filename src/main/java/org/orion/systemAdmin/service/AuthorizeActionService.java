package org.orion.systemAdmin.service;

import org.orion.common.message.DataManager;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.rbac.LoginResult;
import org.orion.common.rbac.User;
import org.orion.common.rbac.UserLoginHistory;
import org.orion.systemAdmin.entity.AppConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AuthorizeActionService {

    private final Logger logger = LoggerFactory.getLogger(AuthorizeActionService.class);
    @Resource
    private UserMaintenanceService userMainService;

    public LoginResult login(User loginUser) {
        int loginStatus = 0;
        LoginResult loginResult = new LoginResult();
        if (loginUser != null) {
            User user = userMainService.getUser(loginUser.getLoginId());
            UserLoginHistory loginHistory = new UserLoginHistory(user);
            if (user != null) {
                Date now = DateUtil.now();
                loginHistory.setLoginTime(now);
                user.setLoginLastAttemptDt(now);
                user.setUpdateAudit(loginUser.getLoginId(), now);
                if (AppConsts.ACCT_LOCKED.equals(user.getAcctStatus())) {
                    loginHistory.setIsSuccess(false);
                    loginHistory.setReason(DataManager.getMessage(AppConsts.CODE_TYPE_ACCT, AppConsts.ACCT_LOCKED));
                    loginStatus = -2;
                } else {
                    boolean pwdValid = Encrtption.verify(loginUser.getPwd(), user.getPwd());
                    if (pwdValid) {
                        user.setLoginFailAttemptCnt(0);
                        switch (user.getAcctStatus()) {
                            case AppConsts.ACCT_ACTIVE :
                                user.setLoginLastSuccessDt(now);
                                loginHistory.setIsSuccess(true);
                                loginHistory.setReason(DataManager.getMessage(AppConsts.CODE_TYPE_ACCT, AppConsts.ACCT_ACTIVE));
                                loginStatus = 1;
                                break;
                            case AppConsts.ACCT_EXPIRED :
                                loginHistory.setIsSuccess(false);
                                loginHistory.setReason(DataManager.getMessage(AppConsts.CODE_TYPE_ACCT, AppConsts.ACCT_EXPIRED));
                                loginStatus = 2;
                                break;
                            case AppConsts.ACCT_INACTIVE :
                                loginHistory.setIsSuccess(false);
                                loginHistory.setReason(DataManager.getMessage(AppConsts.CODE_TYPE_ACCT, AppConsts.ACCT_INACTIVE));
                                loginStatus = 3;
                                break;
                            case AppConsts.ACCT_FROZEN :
                                loginHistory.setIsSuccess(false);
                                loginHistory.setReason(DataManager.getMessage(AppConsts.CODE_TYPE_ACCT, AppConsts.ACCT_FROZEN));
                                loginStatus = 4;
                                break;
                        }

                    } else {
                        //password incorrect
                        user.setLoginFailAttemptCnt(user.getLoginFailAttemptCnt() + 1);
                        if (user.getLoginFailAttemptCnt() == 5) {
                            user.setAcctStatus(AppConsts.ACCT_LOCKED);
                        }
                        loginHistory.setIsSuccess(false);
                        loginHistory.setReason(DataManager.getMessage(AppConsts.CODE_TYPE_ACCT, AppConsts.ACCT_INVPWD));
                        loginStatus = -1;
                    }
                }
            }
            loginResult.setLoginHistory(loginHistory);
            loginResult.setUser(user);
        }
        loginResult.setStatus(loginStatus);
        return loginResult;
    }
}
