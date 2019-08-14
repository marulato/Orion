package org.orion.systemAdmin.controller;

import org.orion.common.basic.AppContext;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.miscutil.HttpUtil;
import org.orion.common.miscutil.ValidationUtil;
import org.orion.systemAdmin.entity.AppConsts;
import org.orion.systemAdmin.entity.User;
import org.orion.systemAdmin.service.AuthorizeActionService;
import org.orion.systemAdmin.service.UserMaintenanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class AuthLoginController {

    @Resource
    private AuthorizeActionService authorizeService;
    @Resource
    private UserMaintenanceService userMainService;
    private final Logger logger = LoggerFactory.getLogger(AuthLoginController.class);

    @RequestMapping("/web/AuthLogin")
    public String initLoginPage() {
        return "systemadmin/login_page";
    }

    @RequestMapping("/web/AuthLogin/ChangePassword")
    public String initChangePwdPage() {
        return "systemadmin/changePassword";
    }

    @RequestMapping("/web/Home")
    public String initHomePage() {
        return "systemadmin/system_info";
    }

    @RequestMapping("/web/AuthLogin/signin")
    @ResponseBody
    public String login(HttpServletRequest request, User loginUser) throws Exception {
        if (loginUser != null) {
            loginUser.setPwd(Encrtption.decryptAES(loginUser.getPwd(), AppConsts.SALT_KEY, true));
            int loginResult = authorizeService.login(loginUser, request);
            if (loginResult == 1) {
                AppContext context = AppContext.getAppContext(request);
                User user = context.getUser();
                if (AppConsts.YES.equals(user.getPwdChgRequired())) {
                    HttpUtil.setSessionAttr(request, "changePwd", AppConsts.YES);
                    return "changePwd";
                }
            }
            return String.valueOf(loginResult);
        }
        return "error";
    }

    @RequestMapping("/web/AuthLogin/ChangePassword/confirm")
    @ResponseBody
    public String changePwd(HttpServletRequest request, String pwd, String npwd) throws Exception {
        String password = Encrtption.decryptAES(pwd, AppConsts.SALT_KEY, true);
        String newPwd = Encrtption.decryptAES(npwd, AppConsts.SALT_KEY, true);
        AppContext context = AppContext.getAppContext(request);
        String changePwd = (String)HttpUtil.getSessionAttr(request, "changePwd");
        if (password.equals(newPwd) && context != null && AppConsts.YES.equals(changePwd)) {
            boolean isValid = ValidationUtil.validatePwdFormat(password);
            if (isValid) {
                User user = context.getUser();
                Date now = DateUtil.now();
                user.setPwd(Encrtption.encryptPassword(password));
                user.setPwdChgRequired(AppConsts.NO);
                user.setPwdLastChgDt(now);
                userMainService.updatePwd(user);
                user.setUpdateAudit(user.getLoginId(), now);
                HttpUtil.clearSession(request, "changePwd");
                HttpUtil.setSessionAttr(request, "login_user", user);
                return "pass";
            } else {
                return "invalid";
            }
        }
        return "notsame";
    }

    @RequestMapping("/web/AuthLogin/logout")
    public void logout(HttpServletRequest request) {
        logger.info("Logout -> Session Elapsed");
        request.getSession().invalidate();
    }

}
