package org.orion.systemAdmin.service;

import org.orion.common.mastercode.ErrorCode;
import org.orion.common.miscutil.StringUtil;
import org.orion.systemAdmin.dao.MasterCodeDao;
import org.orion.systemAdmin.entity.MasterCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MasterCodeService {
    @Resource
    private MasterCodeDao masterCodeDao;

    public ErrorCode getErrorCode(String errorCode) {
        if (!StringUtil.isEmpty(errorCode))
            return masterCodeDao.queryErrorCode(errorCode);
        return null;
    }

    public List<ErrorCode> getAllErrorCodes() {
        return masterCodeDao.queryAllErrorCode();
    }

    public List<ErrorCode> getErrorCodeList(List<String> errorCodeList) {
        if (errorCodeList != null && !errorCodeList.isEmpty())
            return masterCodeDao.batchQueryErrorCode(errorCodeList);
        return null;
    }

    public void createErrorCode(ErrorCode errorCode) {
        if (errorCode != null) {
            masterCodeDao.insertErrorCode(errorCode);
        }
    }

    public List<MasterCode> getAllMasterCode() {
        return masterCodeDao.queryAll();
    }
}
