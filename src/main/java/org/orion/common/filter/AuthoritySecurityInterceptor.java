package org.orion.common.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthoritySecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      /*  if (authoritySecurityService.checkSessionExsits(request)) {
            UserAccount userAccount = (UserAccount) HttpUtil.getSessionAttr(request, "user_account");
            String currentURI = HttpUtil.getURI(request).substring(5);
            //No Param URI
            if (authoritySecurityService.validateAuthority(userAccount, currentURI)) {
                return true;
                //Param Restful API Verification
            } else if(authoritySecurityService.validateAuthority(userAccount, currentURI.substring(0, currentURI.lastIndexOf("/")))){
                return true;
            } else {
                response.sendRedirect("/star/web/dashboard");
            }
        } else {
            response.sendRedirect("/star/web/AuthLogin");
        }*/
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
