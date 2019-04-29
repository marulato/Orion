package org.orion.systemAdmin.dao.provider;

import org.orion.common.dao.BaseSQLProvider;
import org.orion.common.miscutil.CollectionUtil;
import org.orion.systemAdmin.entity.User;

public class UserSqlProvider extends BaseSQLProvider {

    public static final String TABLE = "USER_TBL";

    public UserSqlProvider() {
        super(TABLE);
    }

    public String insertUserSQL() {
        return insert(User.class, CollectionUtil.initialList("acctId"), null);
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
}
