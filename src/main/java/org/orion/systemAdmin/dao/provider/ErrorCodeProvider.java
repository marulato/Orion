package org.orion.systemAdmin.dao.provider;

import org.orion.common.dao.BaseSQLProvider;

import java.util.List;

public class ErrorCodeProvider extends BaseSQLProvider {

    public static final String TABLE = "ERROR_CODE_MC";
    public ErrorCodeProvider() {
        super(TABLE);
    }

    public String queryErrorCodeSQL() {
        String select = selectAll();
        String where = setWhereClause("ERROR_CODE", "=");
        return select + where;
    }

}
