package org.orion.common.cache;

import java.util.List;
import java.util.Map;

public class Cache <T> {

    private Map<Object, T> cacheMap;

    public T get(Object key) {
        return cacheMap.get(key);
    }

}
