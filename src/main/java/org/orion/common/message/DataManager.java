package org.orion.common.message;

import org.orion.common.cache.Cache;
import org.orion.common.cache.CacheManager;

public final class DataManager {

    public static String getErrorMessage(String code) {
        Cache cache = CacheManager.getCache(CacheManager.ERROR_CODE);
        return (String) cache.get(code);
    }
}
