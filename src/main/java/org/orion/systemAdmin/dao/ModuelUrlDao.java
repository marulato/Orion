package org.orion.systemAdmin.dao;

import org.orion.systemAdmin.entity.ModuelUrlAssign;
import java.util.List;

public interface ModuelUrlDao {

    void create(ModuelUrlAssign moduelUrlAssign);

    void batchCreate(List<ModuelUrlAssign> moduelUrlAssignList);
}
