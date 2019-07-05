package org.orion.systemAdmin.service;

import org.orion.common.basic.AppContext;
import org.orion.common.message.DataManager;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.miscutil.HttpUtil;
import org.orion.systemAdmin.entity.AppConsts;
import org.orion.systemAdmin.entity.User;
import org.orion.systemAdmin.entity.UserLoginHistory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class AuthorizeActionService {
    @Resource
    private UserMaintenanceService userMainService;

    public int login(User loginUser, HttpServletRequest request) {
        int loginStatus = 0;
        if (loginUser != null) {
            User user = userMainService.getUser(loginUser.getLoginId());
            if (user != null) {
                UserLoginHistory loginHistory = new UserLoginHistory(user);
                loginHistory.setSessionId(request.getSession().getId());
                Date now = DateUtil.now();
                user.setLoginLastAttemptDt(now);
                user.setUpdateAudit(loginUser.getLoginId(), now);
                if (AppConsts.ACCT_LOCKED.equals(user.getAcctStatus())) {
                    loginHistory.setIsSuccess(false);
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
                            loginHistory.setReason(DataManager.getMessage(AppConsts.CODE_TYPE_ACCT, AppConsts.ACCT_LOCKED));
                        }
                        loginStatus = -1;
                    }
                }
                if (loginStatus == 1) {
                    HttpUtil.setSessionAttr(request, "login_user", user);
                    HttpUtil.setSessionAttr(request, "login_time", now);
                    HttpUtil.setSessionAttr(request, "is_login", true);
                    AppContext.getAppContext(request);
                }
                userMainService.updateUserLogin(user);
            }
        }
        return loginStatus;
    }
}
