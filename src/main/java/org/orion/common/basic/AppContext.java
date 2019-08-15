package org.orion.common.basic;

import org.orion.common.miscutil.HttpUtil;
import org.orion.common.miscutil.SpringUtil;
import org.orion.common.rbac.OrionUserRole;
import org.orion.common.rbac.User;
import org.orion.systemAdmin.service.RbacService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public class AppContext {

    private User user;
    private List<OrionUserRole> userRoles;
    private OrionUserRole currentRole;
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
                RbacService rbacService = SpringUtil.getBean(RbacService.class);
                context.setUserRoles(rbacService.getUserRole(user));
                context.setLoginTime(new Date());
                context.setSessionId(request.getSession().getId());
                HttpUtil.setSessionAttr(request, "AppContext", context);
            }
        }
        return context;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
