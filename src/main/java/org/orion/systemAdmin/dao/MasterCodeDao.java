package org.orion.systemAdmin.dao;

import org.orion.systemAdmin.entity.MasterCode;
import java.util.List;

public interface MasterCodeDao {

    MasterCode queryMasterCode(String type, String code);

    List<MasterCode> queryAll();
}
