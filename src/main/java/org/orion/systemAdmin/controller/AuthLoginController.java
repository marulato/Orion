package org.orion.systemAdmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthLoginController {

    @RequestMapping("/web/AuthLogin")
    public String navigateLoginPage() {
        return "authLogin/authLogin";
    }
}
