package com.redisson;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

/**
 * 专门提供 RedisLimiter 限流基础服务的（提供了通用的能力）
 */
public class RedissonLimiter {


    /**
     * 限流操作
     *
     * @param key 区分不同的限流器，比如不同的用户 id 应该分别统计
     */
    public static  void doRateLimit(String key) {
        RedissonClient redissonClient = RedissonConfig.create();
        // 创建一个名称为user_limiter的限流器，每秒最多访问 2 次
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        // 每当一个操作来了后，请求一个令牌
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new RuntimeException("请求失败");
        }
    }

    public static void main(String[] args) {
        doRateLimit("1");
    }
}
