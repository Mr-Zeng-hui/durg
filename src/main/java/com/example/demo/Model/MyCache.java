package com.example.demo.Model;


import lombok.Data;

/**
 * @author qx
 * @date 2023/7/27
 * @des 自定义缓存实体类
 */
@Data
public class MyCache {

    /**
     * 键
     */
    private String key;

    /**
     * 值
     */
    private Object value;

    /**
     * 过期时间
     */
    private Long expireTime;

}