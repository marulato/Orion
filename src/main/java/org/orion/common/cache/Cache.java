package org.orion.common.cache;

import java.util.Map;

public class Cache <T> {

    private Map<Object, T> cacheMap;
    private String cacheId;

    public T get(Object key) {
        return cacheMap.get(key);
    }

    public void flush() {
        CachePool.flush(cacheId);
    }

    public int getSize() {
        return cacheMap.size();
    }

    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }
}
