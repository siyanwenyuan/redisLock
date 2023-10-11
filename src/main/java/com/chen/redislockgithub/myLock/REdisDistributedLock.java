package com.chen.redislockgithub.myLock;


import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 手写分布式锁
 */

@Data
public class REdisDistributedLock implements Lock {

    @Autowired
    private RedisTemplate redisTemplate;

    private String lockName;
    private String uuidValue;
    private Long expireTime;

    public REdisDistributedLock(RedisTemplate redisTemplate, String lockName) {
        this.redisTemplate = redisTemplate;
        this.lockName = lockName;
        this.uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().getId();
        this.expireTime = 50L;
    }

    @Override
    public void lock() {


        tryLock();
    }

    @Override
    public boolean tryLock() {
        try {
            tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        if (time == -1L) {
            String script = "if redis.call('exists',KEYS[1])==0 or redis.call('hexists',KEYS[1],ARGV[1])==1 then " +
                    "redis.call('hincrby',KEYS[1],ARGV[1],1) " +
                    "redis.call('expire',KEYS[1],ARGV[2]) " +
                    "return 1 " +
                    "else " +
                    "return 0" +
                    "end";


            /**
             * 此处加入自旋的概念，也即是如果加速失败，则自动不断的加锁，一直到成功为止
             */
            while (!(boolean) redisTemplate.execute(new DefaultRedisScript(script, Boolean.class),
                    Arrays.asList(lockName),
                    uuidValue, String.valueOf(expireTime))
            ) {
                //如果失败，则隔一段时间，继续加锁
                try {
                    TimeUnit.MILLISECONDS.sleep(60);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //如果成功，则直接返回'
            return true;

        }

        return false;
    }

    @Override
    public void unlock() {

    }


    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
}
