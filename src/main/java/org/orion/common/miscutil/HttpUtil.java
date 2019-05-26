package org.orion.common.miscutil;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {

    public static final String SESSION_ATTR_LOGIN_CTX		= "loginContext";

    public static Object getSessionAttr(HttpServletRequest request, String name) {
        return request.getSession().getAttribute(name);
    }

    public static void setSessionAttr(HttpServletRequest request, String name, Object object) {
        request.getSession().setAttribute(name, object);
    }

    public static Object getRequestParam(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    public static void setRequestAttr(HttpServletRequest request, String paramName, Object param) {
        request.setAttribute(paramName, param);
    }

    public static String getURL(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    public static String getURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public static void clearSession(HttpServletRequest request, String... names) {
        if (names != null && names.length > 0) {
            for (String name : names) {
                request.getSession().removeAttribute(name);
            }
        }
    }

}
