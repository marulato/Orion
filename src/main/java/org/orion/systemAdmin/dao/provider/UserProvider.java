package org.orion.systemAdmin.dao.provider;

import org.orion.common.dao.BaseSQLProvider;
import org.orion.common.miscutil.CollectionUtil;
import org.orion.systemAdmin.entity.User;

public class UserProvider extends BaseSQLProvider {

    public static final String TABLE = "USER_TBL";

    public UserProvider() {
        super(TABLE);
    }

    public String insertUserSQL() {
        return insert(User.class, null, null);
    }

    public String updateUserByUserIdSQL() {
        String update = updateAll(User.class, CollectionUtil.initialList("acctId", "userId", "domain", "terminal"));
        String where = setWhereClause("USER_ID", "=");
        return update + where;
    }

    public String updateUserByAcctIdSQL() {
        String update = updateAll(User.class, CollectionUtil.initialList("acctId", "userId", "domain", "terminal"));
        String where = setWhereClause("ACCT_ID", "=");
        return update + where;
    }

    public String updateLoginSQL() {
        String update = updateAll(User.class, CollectionUtil.initialList(""));
        String where = setWhereClause("USER_ID", "=");
        return update + where;
    }

    public String getLoginUserSQL() {
        String select = selectAll();
        String where = setWhereClause("USER_ID", "=");
        return select + where;
    }
}
