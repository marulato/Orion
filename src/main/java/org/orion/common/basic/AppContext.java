package org.orion.common.basic;

import org.orion.common.miscutil.HttpUtil;
import org.orion.common.miscutil.SpringUtil;
import org.orion.systemAdmin.entity.OrionUserRole;
import org.orion.systemAdmin.entity.User;
import org.orion.systemAdmin.service.UserMaintenanceService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public class AppContext {

    private User user;
    private List<String> urlList;
    private List<OrionUserRole> userRoles;
    private OrionUserRole currentRole;
    private boolean isLogin;
    private Date loginTime;
    private String sessionId;

    private AppContext(){}

    public static AppContext getAppContext(HttpServletRequest request) {
        User user = (User) HttpUtil.getSessionAttr(request, "login_user");
        AppContext context = null;
        if (user != null) {
            context = (AppContext) HttpUtil.getSessionAttr(request, "AppContext");
            if (context == null) {
                context = new AppContext();
                context.setUser(user);
                UserMaintenanceService userMainService = SpringUtil.getBean(UserMaintenanceService.class);
                context.setUserRoles(userMainService.getUserRole(user));
                context.setUrlList(userMainService.getUrlForUser(user));
                context.setLoginTime((Date) HttpUtil.getSessionAttr(request, "login_time"));
                context.setLogin((boolean) HttpUtil.getSessionAttr(request, "is_login"));
                context.setSessionId(request.getSession().getId());
                HttpUtil.setSessionAttr(request, "AppContext", context);
                HttpUtil.clearSession(request, "login_time", "is_login");
            }
        }
        return context;
    }

    public boolean isValidURL(String url) {
        return urlList.contains(url.substring(url.lastIndexOf("/")));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public List<OrionUserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<OrionUserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public OrionUserRole getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(OrionUserRole currentRole) {
        this.currentRole = currentRole;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
