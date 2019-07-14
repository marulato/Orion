package org.orion.common.dao.crud;

import org.orion.common.basic.BaseEntity;
import org.orion.common.dao.DatabaseSchemaDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CrudManager {

    @Resource
    private DatabaseSchemaDao databaseSchemaDao;

    public void create(BaseEntity entity) {
        if (entity != null) {
            databaseSchemaDao.insert(entity);
        }
    }

    public void update(BaseEntity entity) {
        if (entity != null) {
            databaseSchemaDao.update(entity);
        }
    }
}
