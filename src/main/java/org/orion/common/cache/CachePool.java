package org.orion.common.cache;

import org.orion.common.mastercode.ErrorCode;
import org.orion.common.miscutil.SpringUtil;
import org.orion.common.scheduel.JobScheduelManager;
import org.orion.systemAdmin.entity.MasterCode;
import org.orion.systemAdmin.service.MasterCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachePool {

    private static JobScheduelManager jobScheduelManager = SpringUtil.getBean(JobScheduelManager.class);
    private static MasterCodeService masterCodeService = SpringUtil.getBean(MasterCodeService.class);

    private static Map<Object, ErrorCode> errorCodeCache;
    private static Map<Object, MasterCode> masterCodeCache;

    private static final Logger logger = LoggerFactory.getLogger("Cache Pool");

    private CachePool(){}

    public static void init() {
        long start = System.currentTimeMillis();

        List<ErrorCode> errorList = masterCodeService.getAllErrorCodes();
        errorCodeCache = new HashMap<>();
        for (ErrorCode code : errorList) {
            errorCodeCache.put(code.getErrorCode(), code);
        }

        List<MasterCode> masterCodeList = masterCodeService.getAllMasterCode();
        masterCodeCache = new HashMap<>();
        for (MasterCode masterCode : masterCodeList) {
            masterCodeCache.put(masterCode.getCodeType() + "," + masterCode.getCode(), masterCode);
            masterCodeCache.put(masterCode.getMcId(), masterCode);
        }
        long end = System.currentTimeMillis();
        logger.info("Cache Initialized Successfully, Time cost: " + (end - start) + " ms");

    }

    public static void flush(String cacheId) {
        switch (cacheId) {
            case "ErrorCode" : {
                errorCodeCache.clear();
                List<ErrorCode> errorList = masterCodeService.getAllErrorCodes();
                for (ErrorCode code : errorList) {
                    errorCodeCache.put(code.getErrorCode(), code);
                }
                break;
            }
            case "MasterCode" : {
                masterCodeCache.clear();
                List<MasterCode> masterCodeList = masterCodeService.getAllMasterCode();
                for (MasterCode masterCode : masterCodeList) {
                    masterCodeCache.put(masterCode.getCodeType() + "," + masterCode.getCode(), masterCode);
                }
                break;
            }
        }
    }

    public static Map<Object, ErrorCode> getErrorCodeCache() {
        return errorCodeCache;
    }

    public static Map<Object, MasterCode> getMasterCodeCache() {
        return masterCodeCache;
    }

}
