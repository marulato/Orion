package org.orion.systemAdmin.service;

import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.Encrtption;
import org.orion.systemAdmin.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AuthorizeActionService {
    @Resource
    private UserMaintenanceService userMainService;

    public int login(User loginUser) {
        int loginStatus = 0;
        if (loginUser != null) {
            User user = userMainService.getUser(loginUser.getLoginId());
            if (user != null) {
                Date now = DateUtil.now();
                user.setLoginLastAttemptDt(now);
                user.setUpdateAudit(loginUser.getLoginId(), now);
                if (user.getLoginFailAttemptCnt() > 5) {
                    user.setAcctStatus("L");
                    loginStatus = -5;
                }
                boolean pwdValid = Encrtption.verify(loginUser.getPwd(), user.getPwd());
                if (pwdValid) {
                    user.setLoginLastSuccessDt(now);
                    user.setLoginFailAttemptCnt(0);

                } else {
                    user.setLoginFailAttemptCnt(user.getLoginFailAttemptCnt() + 1);
                }
                userMainService.updateUserLogin(user);
            }
        }
        return loginStatus;
    }
}
