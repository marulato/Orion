package org.orion.common.cache;

import org.orion.common.scheduel.BatchJobEntity;

import java.lang.reflect.Field;
import java.util.Map;

public class CacheManager {

    public static final String BATCH_JOB = "BatchJob";
    public static final String ERROR_CODE = "ErrorCode";

    public static Cache<?> getCache(String cacheId) {
        if ("BatchJob".equals(cacheId)) {
            Cache<BatchJobEntity> cache = new Cache<>();
            setCacheMap(cache, CachePool.getBatchjobCache());
            cache.setCacheId(cacheId);
            return cache;
        }
        return null;
    }

    public void flushAll() {
        CachePool.init();
    }

    private static  <T> void setCacheMap(Cache<T> cache, Map<Object, T> map) {
        if (cache != null && map != null) {
            Class cls = cache.getClass();
            try {
                Field cacheField = cls.getDeclaredField("cacheMap");
                cacheField.setAccessible(true);
                cacheField.set(cache, map);
            } catch (Exception e) {

            }
        }
    }
}
