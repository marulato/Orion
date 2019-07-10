package org.orion.common.validation;

import org.orion.common.miscutil.StringUtil;

public abstract class ValidationUtil {

    public static final String LOGIN_ID         = "\\w{5,20}";
    public static final String PWD_UP_ALPHA     = ".*[A-Z]+.*";
    public static final String PWD_LOW_ALPHA    = ".*[a-z]+.*";
    public static final String PWD_DIGIT        = ".*\\d+.*";
    public static final String PWD_SPECIAL      = ".*[\\~\\!\\@\\#\\$\\%\\&\\.\\*\\?\\^\\<\\>\\=\\+\\-\\,\\/\\_]+.*";
    public static final String EMAIL            = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?";

    public static boolean validateIDFormate(String id) {
        if (!StringUtil.isEmpty(id) && id.length() >= 5 && id.length() <= 20) {
            return id.matches(LOGIN_ID);
        }
        return false;
    }

    public static boolean validatePwdFormat(String pwd) {
        int validate = 0;
        if(!StringUtil.isEmpty(pwd) && pwd.length() >= 6 && pwd.length() <= 24) {
            if (pwd.matches(PWD_UP_ALPHA))
                validate ++;
            if (pwd.matches(PWD_LOW_ALPHA))
                validate ++;
            if (pwd.matches(PWD_DIGIT))
                validate ++;
            if (pwd.matches(PWD_SPECIAL))
                validate ++;
        }
        return validate >= 2;
    }

    public boolean isValidEmail(String email) {
        if (!StringUtil.isEmpty(email)) {
            return email.matches(EMAIL);
        }
        return false;
    }
}
