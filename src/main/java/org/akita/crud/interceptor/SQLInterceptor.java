package org.akita.crud.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Intercepts(value = {@Signature(type = Executor.class, method = "query", args =
                        {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args =
                {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SQLInterceptor implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger("Akita SQL Executor");
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object sqlObject = args[1];
        BoundSql sql = mappedStatement.getBoundSql(sqlObject);
        logger.info(sql.getSql());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
