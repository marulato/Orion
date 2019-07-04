package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.orion.systemAdmin.entity.MasterCode;

import java.util.List;

@Mapper
public interface MasterCodeDao {

    MasterCode queryMasterCode(String type, String code);

    List<MasterCode> queryAll();
}
