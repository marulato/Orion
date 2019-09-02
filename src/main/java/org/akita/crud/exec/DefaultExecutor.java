package org.akita.crud.exec;

import org.akita.common.DefaultSchema;
import org.akita.crud.api.ICrudExecutor;
import org.akita.proxy.MybatisDAOProxy;
import org.orion.common.miscutil.SpringUtil;
import org.springframework.stereotype.Component;

@Component
public class DefaultExecutor implements ICrudExecutor {

    private MybatisDAOProxy daoProxy = SpringUtil.getBean(MybatisDAOProxy.class);
    @Override
    public void insert(DefaultSchema schema) {
        daoProxy.insert(schema);
    }

    @Override
    public void update(DefaultSchema schema) {
        daoProxy.update(schema);
    }

    @Override
    public void delete(DefaultSchema schema) {
        daoProxy.delete(schema);
    }

}
