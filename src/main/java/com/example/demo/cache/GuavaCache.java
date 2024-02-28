package com.example.demo.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GuavaCache {
    private static final Cache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(1000) // 设置缓存的最大容量
            .expireAfterWrite(10, TimeUnit.MINUTES) // 设置写入后10分钟过期
            .build();

    public static String getCachedString(String key) {
        return cache.getIfPresent(key);
    }

    public static void putCachedString(String key, String value) {
        cache.put(key, value);
    }


}
