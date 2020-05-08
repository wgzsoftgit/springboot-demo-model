package com.demomodel.utils.redis.JedisPool.conf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 
@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {
 
    @Value("${spring.redis.host}")
    private String host;
 
    @Value("${spring.redis.port}")
    private int port;
 
    @Value("${spring.redis.timeout}")
    private int timeout;
 
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
 
    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;
 
    @Value("${spring.redis.password}")
    private String password;
 
    @Value("${spring.redis.block-when-exhausted}")
    private boolean  blockWhenExhausted;
 
    @Bean
    public JedisPool redisPoolFactory()  throws Exception{
      System.err.println("JedisPool注入成功！！");
      System.err.println("redis地址：" + host + ":" + port+password);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
      //  JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);//有密码的连接
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout); //无密码连接
        return jedisPool;
    }
 
}
//：https://blog.csdn.net/zhulier1124/java/article/details/82193182