package org.orion.common.filter;

import org.orion.common.basic.AppContext;
import org.orion.common.miscutil.HttpUtil;
import org.orion.systemAdmin.service.AuthorizeActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorityInterceptor implements HandlerInterceptor {
    @Resource
    private AuthorizeActionService authorizeActionService;
    private final Logger logger = LoggerFactory.getLogger("Authority Monitor");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String originUri = HttpUtil.getURI(request);
        String uri = originUri.replace("/orion", "");
        if (uri.contains(".")) {
            String urlSuffix = uri.substring(uri.lastIndexOf("."));
            if (urlSuffix.contains("js") || urlSuffix.contains("css") || urlSuffix.contains("png") || urlSuffix.contains("jpg") ||urlSuffix.contains("gif") || urlSuffix.contains("ttf"))
                return true;
        }
        if (authorizeActionService.checkSessionValidity(request)) {
            AppContext context = AppContext.getAppContext(request);
            if (context.getUrlList().contains(uri)) {
                return true;
            } else {
                logger.info("User: + " + context.getUser().getLoginId() + " doesn't have the permission to visit the url: " + uri);
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
