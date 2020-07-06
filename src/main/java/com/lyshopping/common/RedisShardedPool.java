package com.lyshopping.common;

import com.lyshopping.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuying
 * 分片的redis（redis分布式）
 **/
public class RedisShardedPool {
    //jedis连接池
    private static ShardedJedisPool pool;//sharded jedis连接池

    //最大的连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")) ;

    //在jedispool中最大的idle状态（空闲的）jedis实例的个数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","20"));

    //在jedispool中最小的idle状态（空闲的）jedis实例的个数
    private static Integer minIdle =Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","20")) ;

    //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true则得到的jedis实例肯定是可以用的
    private static Boolean testOnBorrow =Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true")) ;

    //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true则放回jedispool的jedis实例肯定是可以用的。
    private static Boolean testOnReturn =Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true")) ;

    //redisip
    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");

    //redis
    private static Integer redis1Port =Integer.parseInt(PropertiesUtil.getProperty("redis1.port")) ;

    //redisip
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");

    //redis
    private static Integer redis2Port =Integer.parseInt(PropertiesUtil.getProperty("redis2.port")) ;


    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽时是否阻塞,false会抛出异常，true会阻塞到超时，超时后会报超时异常。默认为true
        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        //假如redis是有密码的
        //info1.setPassword("xxxx");
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    //类创建的时候调用
    static{
        initPool();
    }

    //对于外部的类，将类的实例开放出去
    public static ShardedJedis getJedis(){
        return  pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }

    //测试
//    public static void main(String[] args) {
//            ShardedJedis jedis = pool.getResource();
//            for(int i=0; i<10;i++){
//            jedis.set("key"+i, "value"+i);
//        }
//            returnResource(jedis);
//            //临时调用，销毁连接池中的所有连接
//            //pool.destroy();
//            System.out.println("progrm is end");
//    }
}
