package org.akita.crud.exec;

import org.akita.common.DefaultSchema;
import org.akita.crud.api.ICrudExecutor;

public class AbstractExecutor implements ICrudExecutor {

    @Override
    public void insert(DefaultSchema schema) {

    }

    @Override
    public void update(DefaultSchema schema) {

    }

    @Override
    public void delete(DefaultSchema schema) {

    }

    @Override
    public <E extends DefaultSchema> E selectAll(E schema) {
        return null;
    }
}
