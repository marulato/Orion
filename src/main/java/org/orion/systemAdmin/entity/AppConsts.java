package org.orion.systemAdmin.entity;

public class AppConsts {

    public static final String SALT_KEY         = "OrionWebPassword";
    public static final String REQUEST_KEY      = "OrionWebRequests";

    public static final String CODE_TYPE_ACCT   = "ACT";
    public static final String ACCT_ACTIVE      = "A";
    public static final String ACCT_LOCKED      = "L";
    public static final String ACCT_INACTIVE    = "I";
    public static final String ACCT_EXPIRED     = "E";
    public static final String ACCT_FROZEN      = "F";
    public static final String ACCT_INVPWD      = "P";

    public static final String CODE_TYPE_AUDIT      = "AUT";
    public static final String AUDIT_BEFOR_MODIFY   = "BM";
    public static final String AUDIT_AFTER_MODIFY   = "AM";
    public static final String AUDIT_AFTER_INSERT   = "AI";
    public static final String AUDIT_BEFOR_DELETE   = "BD";

    public static final String YES     = "Y";
    public static final String NO      = "N";

    public static final String INVALID_REQUEST  = "000";
    public static final String RESPONSE_SUCCESS = "200";
    public static final String RESPONSE_ERROR   = "500";

    public static final String VALIDATION_ERROR     = "350";
    public static final String VALIDATION_PASSED    = "300";

    public static final String ACTION_ADD    = "Add";
    public static final String ACTION_MODIFY = "Modify";

    public static final String DEFAULT_TRIGGER_GROUP    = "OrionTriggerGroup";
    public static final String DEFAULT_TRIGGER_NAME    = "OrionTrigger";
}
