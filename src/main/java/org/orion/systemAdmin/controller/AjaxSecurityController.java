package org.orion.systemAdmin.controller;

import org.orion.common.miscutil.DateUtil;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.validation.Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AjaxSecurityController {

    @RequestMapping("/web/Security/RequestValidation/token")
    @ResponseBody
    public String generateToken() {
        String token = Encrtption.generateToken();
        Token.setToken(token);
        Token.setGenerateDate(DateUtil.now());
        Token.setInvalidDate(DateUtil.addMinutes(Token.getGenerateDate(), 15));
        return token;
    }

    @RequestMapping("/web/error")
    public String internalServerError() {
        return "errorpage/500";
    }
}
