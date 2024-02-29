package com.example.demo.util;


import com.example.demo.Model.MyCache;

import java.time.Duration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author qx
 * @date 2023/7/27
 * @des 自定义本地缓存工具类
 */
public class CacheUtil {

    /**
     * 缓存数据Map
     */
    private static final Map<String, MyCache> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 定时器线程池，用于清除过期缓存
     */
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    static {
        // 注册一个定时线程任务，服务启动1秒之后，每隔500毫秒执行一次
        // 定时清理过期缓存
        executorService.scheduleAtFixedRate(CacheUtil::clearCache, 1000, 500, TimeUnit.MINUTES);
    }

    /**
     * 添加缓存
     *
     * @param key    缓存键
     * @param value  缓存值
     * @param expire 过期时间，单位秒
     */
    public static void put(String key, Object value, long expire) {
        MyCache myCache = new MyCache();
        myCache.setKey(key);
        myCache.setValue(value);
        if (expire > 0) {
            long expireTime = System.currentTimeMillis() + Duration.ofSeconds(expire).toMillis();
            myCache.setExpireTime(expireTime);
        }
        CACHE_MAP.put(key, myCache);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @return 缓存数据
     */
    public static Object get(String key) {
        if (CACHE_MAP.containsKey(key)) {
            return CACHE_MAP.get(key).getValue();
        }
        return null;
    }

    /**
     * 移除缓存
     *
     * @param key 缓存键
     */
    public static void remove(String key) {
        CACHE_MAP.remove(key);
    }

    /**
     * 清理过期的缓存数据
     */
    private static void clearCache() {
        if (CACHE_MAP.size() <= 0) {
            return;
        }
        // 判断是否过期 过期就从缓存Map删除这个元素
        CACHE_MAP.entrySet().removeIf(entry -> entry.getValue().getExpireTime() != null && entry.getValue().getExpireTime() > System.currentTimeMillis());
    }
}