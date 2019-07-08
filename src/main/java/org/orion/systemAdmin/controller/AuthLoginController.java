package org.orion.systemAdmin.controller;

import org.orion.common.mastercode.ErrorCode;
import org.orion.common.miscutil.Validation;
import org.orion.systemAdmin.entity.User;
import org.orion.systemAdmin.service.AuthorizeActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AuthLoginController {

    @Resource
    private AuthorizeActionService authorizeService;
    private final Logger logger = LoggerFactory.getLogger(AuthLoginController.class);

    @RequestMapping("/web/AuthLogin")
    public String initLoginPage() {
        return "authLogin/authLogin";
    }

    @RequestMapping("/web/AuthLogin/signin")
    @ResponseBody
    public String login(HttpServletRequest request, User loginUser) {
        if (loginUser != null) {
            List<ErrorCode> errorCodes = Validation.doValidate(loginUser);
            int loginResult = authorizeService.login(loginUser, request);
            switch (loginResult) {
                case 1 :
                    logger.info("User Login Successfully: ");
                    break;
            }
            return String.valueOf(loginResult);
        }
        return "error";
    }

    @RequestMapping("/web/AuthLogin/logout")
    public void logout(HttpServletRequest request) {
        logger.info("Logout -> Session Elapsed");
        request.getSession().invalidate();
    }

}
