package org.orion.systemAdmin.service;

import org.orion.common.mastercode.ErrorCode;
import org.orion.common.miscutil.StringUtil;
import org.orion.systemAdmin.dao.ErrorCodeDao;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class MasterCodeService {
    @Resource
    private ErrorCodeDao errorCodeDao;

    public ErrorCode getErrorCode(String errorCode) {
        if (!StringUtil.isEmpty(errorCode))
            return errorCodeDao.queryErrorCode(errorCode);
        return null;
    }

    public List<ErrorCode> getErrorCodeList(List<String> errorCodeList) {
        if (errorCodeList != null && !errorCodeList.isEmpty())
            return errorCodeDao.batchQueryErrorCode(errorCodeList);
        return null;
    }

    public void createErrorCode(ErrorCode errorCode) {
        if (errorCode != null) {
            errorCodeDao.insertErrorCode(errorCode);
        }
    }
}
