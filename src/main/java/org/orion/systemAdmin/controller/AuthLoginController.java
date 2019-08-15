package org.orion.systemAdmin.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.orion.common.basic.AppContext;
import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.miscutil.HttpUtil;
import org.orion.common.miscutil.ValidationUtil;
import org.orion.common.rbac.User;
import org.orion.common.rbac.UserLoginHistory;
import org.orion.configration.authority.ShiroRealm;
import org.orion.systemAdmin.entity.AppConsts;
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

    @RequiresUser
    @RequestMapping("/web/AuthLogin/ChangePassword")
    public String initChangePwdPage() {
        return "systemadmin/changePassword";
    }

    @RequiresRoles(value = {"SYSTEMADMIN"})
    @RequestMapping("/web/Home")
    public String initSysHomePage() {
        return "systemadmin/system_info";
    }

    @RequestMapping("/web/Index")
    @RequiresUser
    public String initNormalHomePage() {
        return "systemadmin/dashboard";
    }

    @RequestMapping("/web/AuthLogin/signin")
    @ResponseBody
    public String login(HttpServletRequest request, User loginUser) throws Exception {
        if (loginUser != null) {
            loginUser.setPwd(Encrtption.decryptAES(loginUser.getPwd(), AppConsts.SALT_KEY, true));
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken();
            token.setUsername(loginUser.getLoginId());
            token.setPassword(loginUser.getPwd().toCharArray());
            try {
                subject.login(token);
                int status = ShiroRealm.getLoginResult().getStatus();
                HttpUtil.setSessionAttr(request, "login_user", ShiroRealm.getLoginResult().getUser());
                if (status == 1) {
                    AppContext context = AppContext.getAppContext(request);
                    User user = context.getUser();
                    if (AppConsts.YES.equals(user.getPwdChgRequired())) {
                        HttpUtil.setSessionAttr(request, "changePwd", AppConsts.YES);
                        return "changePwd";
                    }
                }
                if (status != 0) {
                    User user = ShiroRealm.getLoginResult().getUser();
                    UserLoginHistory loginHistory = ShiroRealm.getLoginResult().getLoginHistory();
                    userMainService.updateUserLogin(user);
                    userMainService.createLoginAudit(loginHistory);
                }
                return String.valueOf(status);
            } catch (Exception e) {
                if (e instanceof AuthenticationException) {
                    logger.info("LOGIN DENIED -> " + loginUser.getLoginId());
                } else {
                    logger.error("用户登陆时发生异常", e);
                }
            }

        }
        return "error";
    }

    @RequestMapping("/web/AuthLogin/ChangePassword/confirm")
    @RequiresUser
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
                user.setUpdateAudit(user.getLoginId(), now);
                userMainService.updatePwd(user);
                HttpUtil.clearSession(request, "changePwd");
                HttpUtil.setSessionAttr(request, "login_user", user);
                return "pass";
            } else {
                return "invalid";
            }
        }
        return "notsame";
    }

    @RequestMapping("/web/logout")
    public String logout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        request.getSession().invalidate();
        return "systemadmin/login_page";
    }

}
