package org.orion.systemAdmin.controller;

import org.orion.common.miscutil.WebFileHelper;
import org.orion.common.scheduel.JobScheduelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthLoginController {

    @Resource
    private JobScheduelManager jobScheduelManager;
    private final Logger logger = LoggerFactory.getLogger(AuthLoginController.class);
    @RequestMapping("/web/AuthLogin")
    public String navigateLoginPage() {
        return "authLogin/authLogin";
    }

    @RequestMapping("/web/testjob")
    @ResponseBody
    public String doJob(HttpServletResponse response) throws Exception {
        WebFileHelper.download("D:\\", "Wallpapers.zip", response);
        return "ok";
    }

    @RequestMapping("/web/delete")
    @ResponseBody
    public String delete() {
        jobScheduelManager.deleteJob("000", "test");
        jobScheduelManager.deleteJob("111", "test");

        return "oooh!";
    }
}
