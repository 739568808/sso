package com.iccgame.ssoserver.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public boolean set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean set(String key, String value, long time, TimeUnit timeUnit){
        try {
            if(time>0){
                stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
            }else{
                set(key, value);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean del(String key){
        try {
            return stringRedisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String get(String key){
        return key==null?"":stringRedisTemplate.opsForValue().get(key);
    }

    public boolean hasKey(String key){
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获取key
     * @param prefix 业务名
     * @param token token值
     * @return
     */
    public String getSSOKey(String prefix,String token){
        StringBuilder sb = new StringBuilder();
        sb.append(prefix)
                .append(":")
                .append(token);

        return sb.toString();
    }
}

