package com.java_api.demo;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;
//import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.concurrent.TimeUnit;

public class ExpiringCache extends ConcurrentMapCache {

    private final Cache<Object, Object> cache;

    public ExpiringCache(String name, long expiration, TimeUnit timeUnit) {
        super(name);
        this.cache = Caffeine.newBuilder()
                .expireAfterAccess(expiration, timeUnit)
                .build();
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = cache.getIfPresent(key);
        return (value != null ? new SimpleValueWrapper(value) : null);
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }
}
