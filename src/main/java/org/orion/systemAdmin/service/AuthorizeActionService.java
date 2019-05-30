package org.orion.systemAdmin.service;

import org.orion.common.mastercode.ErrorCode;
import org.orion.common.miscutil.Validation;
import org.orion.systemAdmin.dao.UserDao;
import org.orion.systemAdmin.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthorizeActionService {
    @Resource
    private UserDao userDao;

    public void createUser(User user) {
        if (user != null) {
            List<ErrorCode> errorCodes = Validation.doValidate(user);
            if (errorCodes != null && !errorCodes.isEmpty()) {

            } else {
                userDao.insert(user);
            }
        }
    }

    public int login(HttpServletRequest request, User user) {

        return 0;
    }
}
