package org.orion.common.cache;

import org.orion.common.mastercode.ErrorCode;
import org.orion.common.scheduel.BatchJobEntity;
import org.orion.common.scheduel.JobScheduelManager;
import org.orion.systemAdmin.service.MasterCodeService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachePool {

    @Resource
    private static JobScheduelManager jobScheduelManager;
    @Resource
    private static MasterCodeService masterCodeService;

    private static Map<Object, BatchJobEntity> batchjobCache;
    private static Map<Object, ErrorCode> errorCodeCache;

    private CachePool(){}

    public static void init() {
        List<BatchJobEntity> jobList = jobScheduelManager.getAllBatchJobs();
        batchjobCache = new HashMap<>();
        for (BatchJobEntity job : jobList) {
            batchjobCache.put(job.getJobId(), job);
        }

        List<ErrorCode> errorList = masterCodeService.getAllErrorCodes();
        errorCodeCache = new HashMap<>();
        for (ErrorCode code : errorList) {
            errorCodeCache.put(code.getErrorCode(), code);
        }

    }

    public static void refresh(String cacheId) {

    }

    public static Map<Object, BatchJobEntity> getBatchjobCache() {
        return batchjobCache;
    }

    public static Map<Object, ErrorCode> getErrorCodeCache() {
        return errorCodeCache;
    }

}