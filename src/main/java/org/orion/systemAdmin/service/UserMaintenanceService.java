package org.orion.systemAdmin.service;

import org.orion.systemAdmin.dao.ModuelUrlDao;
import org.orion.systemAdmin.dao.RoleModuelDao;
import org.orion.systemAdmin.dao.UserDao;
import org.orion.systemAdmin.dao.UserRoleDao;
import org.orion.systemAdmin.entity.ModuelUrlAssign;
import org.orion.systemAdmin.entity.OrionUserRole;
import org.orion.systemAdmin.entity.RoleModuelAssign;
import org.orion.systemAdmin.entity.User;
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
        userDao.updateAfterLogin(user);
    }

    public List<OrionUserRole> getUserRole(User user) {
        return userRoleDao.queryRole(user.getLoginId());
    }

    public List<ModuelUrlAssign> getModuelUrlAssignForUser(User user) {
        List<ModuelUrlAssign> moduelUrlList = new ArrayList<>();
        if (user != null) {
            List<OrionUserRole> userRoles = userRoleDao.queryRole(user.getLoginId());
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
