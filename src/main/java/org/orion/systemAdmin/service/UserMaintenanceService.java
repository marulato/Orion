package org.orion.systemAdmin.service;

import org.orion.systemAdmin.dao.UserDao;
import org.orion.systemAdmin.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserMaintenanceService {
    @Resource
    private UserDao userDao;

    public User getUser(String loginId) {
        return userDao.query(loginId);
    }

    public void updateUserLogin(User user) {
        userDao.updateAfterLogin(user);
    }


}
