package com.demomodel.utils.RedisLoack.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

/**
 * 基于redis setnx的 分布式锁 实, 前提是所有的锁都要有锁定时间.
 * 获取锁的时候,需要指定value,在unlock的时候,会根据value判断是否remove
 */
public class RedisLockUtil2 {
    private static final String LOCK_PREFIX = "LOCK";
    private static final Integer DEFAULT_LOCK_TIME = 10;// 默认锁定时间秒
    private static final Long DEFAULT_SLEEP_TIME = 100L;// 默认sleep时间,100毫秒
    private static Logger logger = LoggerFactory.getLogger(RedisLockUtil2.class);

    /**
     * 获取缓存的value,随机值,使不同的锁value不同 (多服务器可以使用redis时间+客户端标识等)
     *
     * @return
     */
    public static String getLockValue() {
        int random = (int) ((Math.random() * 9 + 1) * 100000);
        long now = System.currentTimeMillis();
        return String.valueOf(now) + String.valueOf(random);
    }

    /**
     * 获取锁,如果失败,自动重试
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean lock(JedisPool jedisPool, String key, String value) {
        return lock(jedisPool, key, value, DEFAULT_LOCK_TIME);
    }

    /**
     * 获取锁,如果失败,自动重试
     *
     * @param key
     * @param value
     * @param lockTime 获取成功后的锁定时间
     * @return
     */
    public static boolean lock(JedisPool jedisPool, String key, String value, int lockTime) {
        return lock(jedisPool, key, value, lockTime, true);
    }

    private static boolean lock(JedisPool jedisPool, String key, String value, int lockTime, boolean reTry) {
        return lock(jedisPool, key, value, lockTime, reTry, 0, false, 0);
    }

    /**
     * 获取锁,如果失败,直接返回false
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean tryLock(JedisPool jedisPool, String key, String value) {
        return tryLock(jedisPool, key, value, DEFAULT_LOCK_TIME);
    }

    /**
     * 获取锁,如果失败,直接返回false
     *
     * @param key
     * @param value
     * @param lockTime 获取成功后的锁定时间
     * @return
     */
    public static boolean tryLock(JedisPool jedisPool, String key, String value, int lockTime) {
        return lock(jedisPool, key, value, lockTime, false);
    }

    /**
     * 尝试获取锁,如果获取失败,重试,直到成功或超出指定时间
     *
     * @param key
     * @param value
     * @param lockTime 获取成功后的锁定时间
     * @param timeOut  获取锁等待超时时间
     * @return
     */
    public static boolean tryLock(JedisPool jedisPool, String key, String value, int lockTime, long timeOutMillis) {
        return lock(jedisPool, key, value, lockTime, true, 0, true, timeOutMillis);
    }

    /**
     * 释放锁,key对应的value于参数value一致,才删除key
     *
     * @param key
     * @param value
     */
    public static boolean unlock(JedisPool jedisPool, String key, String value) {
        String fullKey = getFullKey(key);
        boolean success = JedisUtil.unlock(jedisPool, fullKey, value);
        if (success) {
            logger.info("unlock success ; key:" + key + ",value:" + value);
        } else {
            logger.info("unlock failed ; key:" + key + ",value:" + value);
        }
        return success;
    }

    /**
     * 获取锁
     *
     * @param key
     * @param value
     * @param lockTime      锁定时间
     * @param reTry         失败是否重试
     * @param curTryTime    当前尝试次数
     * @param needTimeOut   是否需要判断超时时间
     * @param timeOutMillis 尝试超时时间(毫秒)
     * @return
     */
    private static boolean lock(JedisPool jedisPool, String key, String value, int lockTime, boolean reTry, int curTryTime,
                                boolean needTimeOut, long timeOutMillis) {

        logger.info(Thread.currentThread().getName() + ",lock come in ; key:" + key + ",value:" + value
                + ",lockTime:" + lockTime + ",reTry:" + reTry + ",curTryTime:" + curTryTime + ",needTimeOut:"
                + needTimeOut + ",timeOutMillis:" + timeOutMillis);

        curTryTime++;
        String fullKey = getFullKey(key);

        // setnx 并设置超时时间
        boolean success = JedisUtil.setnx(jedisPool, fullKey, value, (long) lockTime * 1000);
        // 获取成功,直接返回
        if (success) {

            logger.info("lock success ; key:" + key + ",value:" + value + ",lockTime:" + lockTime + ",reTry:"
                    + reTry + ",curTryTime:" + curTryTime + ",needTimeOut:" + needTimeOut + ",timeOutMillis:"
                    + timeOutMillis);

            return true;
        }

        // 获取失败,不需要重试,直接返回
        if (!reTry) {
            logger.info("lock failed ; key:" + key + ",value:" + value + ",lockTime:" + lockTime + ",reTry:"
                    + reTry + ",curTryTime:" + curTryTime + ",needTimeOut:" + needTimeOut + ",timeOutMillis:"
                    + timeOutMillis);

            return false;
        }

        // 获取失败, 且已超时,返回
        if (needTimeOut && timeOutMillis <= 0) {
            logger.info("lock failed ; key:" + key + ",value:" + value + ",lockTime:" + lockTime + ",reTry:"
                    + reTry + ",curTryTime:" + curTryTime + ",needTimeOut:" + needTimeOut + ",timeOutMillis:"
                    + timeOutMillis);

            return false;
        }

        // 获取sleep时间  &&
        long sleepMillis = getSleepMillis(needTimeOut, timeOutMillis);

        // sleep后重新获取锁
        sleep(sleepMillis);

        // 大于100次,打印warning日志
        if (curTryTime > 100) {
            logger.warn("lock warning ; key:" + key + ",value:" + value + ",lockTime:" + lockTime + ",reTry:"
                    + reTry + ",curTryTime:" + curTryTime + ",needTimeOut:" + needTimeOut + ",timeOutMillis:"
                    + timeOutMillis);

        }

        return lock(jedisPool, key, value, lockTime, reTry, curTryTime, needTimeOut, timeOutMillis);
    }

    private static long getSleepMillis(boolean needTimeOut, long timeOutMillis) {
        long sleepMillis = DEFAULT_SLEEP_TIME;
        if (needTimeOut) {
            timeOutMillis = timeOutMillis - DEFAULT_SLEEP_TIME;
            if (timeOutMillis < DEFAULT_SLEEP_TIME) {
                sleepMillis = timeOutMillis;
            }
        }
        return sleepMillis;
    }

    private static void sleep(long sleepMillis) {
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getFullKey(String key) {
        return LOCK_PREFIX + ":" + key;
    }
}