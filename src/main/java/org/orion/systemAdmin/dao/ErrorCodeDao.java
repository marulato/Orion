package org.orion.systemAdmin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.orion.common.mastercode.ErrorCode;
import org.orion.systemAdmin.dao.provider.ErrorCodeProvider;

import java.util.List;

@Mapper
public interface ErrorCodeDao {

    ErrorCode queryErrorCode(String errorCode);

    List<ErrorCode> batchQueryErrorCode(@Param("errs") List<String> errorCodes);

    void insertErrorCode(ErrorCode errorCode);
}
