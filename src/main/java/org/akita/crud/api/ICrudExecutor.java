package org.akita.crud.api;

import org.akita.common.DefaultSchema;

public interface ICrudExecutor {

    void insert(DefaultSchema schema);
    void update(DefaultSchema schema);
    void delete(DefaultSchema schema);
    <E extends DefaultSchema> E selectAll(E schema);

}
