package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.orion.common.mastercode.ErrorCode;
import org.orion.systemAdmin.entity.MasterCode;

import java.util.List;

@Mapper
public interface MasterCodeDao {

    MasterCode queryMasterCode(String type, String code);

    List<MasterCode> queryAll();

    ErrorCode queryErrorCode(String errorCode);

    List<ErrorCode> queryAllErrorCode();

    List<ErrorCode> batchQueryErrorCode(@Param("errs") List<String> errorCodes);

    void insertErrorCode(ErrorCode errorCode);
}
