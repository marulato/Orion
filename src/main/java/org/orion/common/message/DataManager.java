package org.orion.common.message;

import org.orion.common.cache.Cache;
import org.orion.common.cache.CacheManager;
import org.orion.common.mastercode.ErrorCode;
import org.orion.systemAdmin.entity.MasterCode;

public final class DataManager {

    public static String getErrorMessage(String code) {
        ErrorCode errorCode = getErrorCode(code);
        if (errorCode != null) {
            return errorCode.getErrorDesc();
        }
        return null;
    }

    public static ErrorCode getErrorCode(String code) {
        Cache cache = CacheManager.getCache(CacheManager.ERROR_CODE);
        return (ErrorCode) cache.get(code);
    }

    public static MasterCode getCode(String codeType, String code) {
        Cache cache = CacheManager.getCache(CacheManager.MASTER_CODE);
        return (MasterCode) cache.get(codeType + "," + code);
    }

    public static String getMessage(String codeType, String code) {
        MasterCode masterCode = getCode(codeType, code);
        if (masterCode != null)
            return masterCode.getDescription();
        return null;
    }
}
