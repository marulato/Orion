package org.orion.common.dao;

import org.orion.common.miscutil.StringUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Component
public class Redis {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
    private HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();

    public void set(final String key, Object value) {
        valueOperations.set(key, value);
    }

    public void set(final String key, Object value, int hours) {
        valueOperations.set(key, value);
        redisTemplate.expire(key, hours, TimeUnit.HOURS);
    }

    public void set(final String key, Object value, long seconds) {
        valueOperations.set(key, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public void set(final String key, Object value, long time, TimeUnit timeUnit) {
        valueOperations.set(key, value);
        redisTemplate.expire(key, time, timeUnit);
    }

    public boolean exsits(String key) {
        return redisTemplate.hasKey(key);
    }

    public Object get(String key) {
        return valueOperations.get(key);
    }

    public void setHash(final String key, String hk, Object object) {
        hashOperations.put(key, hk, object);
    }

    public Object getHash(String key, String hk) {
        return hashOperations.get(key, hk);
    }

    public void autoSetHash(String key, Object object) {
        if (!StringUtil.isEmpty(key) && object != null) {
            Class objClass = object.getClass();
            Field[] fields = objClass.getDeclaredFields();
            String[] fieldNames = new String[fields.length];
            String[] getters = new String[fields.length];
            for (int i = 0; i <fields.length; i++) {
                fieldNames[i] = fields[i].getName();
                getters[i] = "get" + StringUtil.toUpperCaseByIndex(fieldNames[i], 0);
            }
            for (int j = 0; j < getters.length; j++) {
                try {
                    Method getterMethod = objClass.getDeclaredMethod(getters[j]);
                    getterMethod.setAccessible(true);
                    Object value = getterMethod.invoke(object);
                    hashOperations.put(key, fieldNames[j], value);
                } catch (Exception e) {
                    try {
                        fields[j].setAccessible(true);
                        Object value = fields[j].get(object);
                        hashOperations.put(key, fieldNames[j], value);
                    } catch (Exception e1) {
                        continue;
                    }
                }
            }
        }
    }

    public <E> E autoGetHash(String key, Class<E> objClass) {
        E entity = null;
        if (!StringUtil.isEmpty(key) && objClass != null) {
            Field[] fields = objClass.getDeclaredFields();
            String[] fieldNames = new String[fields.length];
            String[] setters = new String[fields.length];
            try {
                entity = objClass.getConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
            for (int i = 0; i < fields.length; i++) {
                fieldNames[i] = fields[i].getName();
                setters[i] = "set" + StringUtil.toUpperCaseByIndex(fieldNames[i], 0);
                Object value = getHash(key, fieldNames[i]);
                try {
                    Method setterMethod = objClass.getDeclaredMethod(setters[i]);
                    setterMethod.setAccessible(true);
                    setterMethod.invoke(entity, value);

                } catch (Exception e) {

                }
            }
        }
        return entity;
    }


}
