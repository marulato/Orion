package org.orion.common.cache;

import org.orion.common.mastercode.ErrorCode;
import org.orion.systemAdmin.entity.MasterCode;

import java.lang.reflect.Field;
import java.util.Map;

public class CacheManager {

    public static final String ERROR_CODE = "ErrorCode";
    public static final String MASTER_CODE = "MasterCode";

    public static Cache<?> getCache(String cacheId) {

        if (MASTER_CODE.equals(cacheId)) {
            Cache<MasterCode> cache = new Cache<>();
            setCacheMap(cache, CachePool.getMasterCodeCache());
            cache.setCacheId(cacheId);
            return cache;
        }

        if (ERROR_CODE.equals(cacheId)) {
            Cache<ErrorCode> cache = new Cache<>();
            setCacheMap(cache, CachePool.getErrorCodeCache());
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
