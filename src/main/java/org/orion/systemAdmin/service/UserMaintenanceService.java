package org.orion.systemAdmin.service;

import org.orion.common.rbac.OrionUserRole;
import org.orion.common.rbac.User;
import org.orion.common.rbac.UserLoginHistory;
import org.orion.common.rbac.UserProfile;
import org.orion.systemAdmin.dao.ModuelUrlDao;
import org.orion.systemAdmin.dao.RoleModuelDao;
import org.orion.systemAdmin.dao.UserDao;
import org.orion.systemAdmin.dao.UserRoleDao;
import org.orion.systemAdmin.entity.ModuelUrlAssign;
import org.orion.systemAdmin.entity.RoleModuelAssign;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserMaintenanceService {
    @Resource
    private UserDao userDao;
    @Resource
    private ModuelUrlDao moduelUrlDao;
    @Resource
    private RoleModuelDao roleModuelDao;
    @Resource
    private UserRoleDao userRoleDao;

    public User getUser(String loginId) {
        return userDao.query(loginId);
    }

    public void updateUserLogin(User user) {
        if (user != null) {
            userDao.updateAfterLogin(user);
        }
    }

    public void createLoginAudit(UserLoginHistory loginHistory) {
        if (loginHistory != null) {
            userDao.createHistory(loginHistory);
        }
    }

    public List<OrionUserRole> getUserRole(User user) {
        if (user != null) {
            return userRoleDao.queryRole(user.getUserId());
        }
        return null;
    }

    public void updatePwd(User user) {
        if (user != null) {
            userDao.updatePassword(user);
        }
    }

    public void createProfile(User user, UserProfile profile) {
        if (user != null && profile != null) {
            User actualUser = getUser(user.getLoginId());
            if (user.equals(actualUser) && user.getUserId() == profile.getUserId()) {
                userDao.createProfile(profile);
            }
        }
    }

    public UserProfile getProfileForUser(User user) {
        if (user != null) {
            return userDao.queryUserProfile(user);
        }
        return null;
    }

    public List<ModuelUrlAssign> getModuelUrlAssignForUser(User user) {
        List<ModuelUrlAssign> moduelUrlList = new ArrayList<>();
        if (user != null) {
            List<OrionUserRole> userRoles = userRoleDao.queryRole(user.getUserId());
            for (OrionUserRole userRole : userRoles) {
                List<RoleModuelAssign> roleList = roleModuelDao.queryRole(userRole.getRoleId());
                for (RoleModuelAssign moduel : roleList) {
                    moduelUrlList.add(moduelUrlDao.queryUrlByFunc(moduel.getFuncId()));
                }
            }
        }
        return moduelUrlList;
    }

    public List<String> getUrlForUser(User user) {
        List<String> urlList = new ArrayList<>();
        if (user != null) {
            List<ModuelUrlAssign> moduelUrlList = getModuelUrlAssignForUser(user);
            for (ModuelUrlAssign moduelUrl : moduelUrlList) {
                urlList.add(moduelUrl.getUrl());
            }
        }
        return urlList;
    }

}
