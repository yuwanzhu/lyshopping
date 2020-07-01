package com.lyshopping.util;

import com.lyshopping.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * @author liuying
 * 分布式redis工具类
 **/
@Slf4j
public class RedisShardedPoolUtil {

        /**
         * 设置key的有效期，单位是秒
         * @param key
         * @Param exTime
         * @Return
         */
        public static Long expire(String key,int exTime){
            ShardedJedis jedis = null;
            Long result = null;

            try {
                jedis = RedisShardedPool.getJedis();
                result = jedis.expire(key,exTime);
            } catch (Exception e) {
                log.error("expire key:{} error",key,e);
                RedisShardedPool.returnBrokenResource(jedis);
                return result;
            }
            RedisShardedPool.returnResource(jedis);
            return result;
        }
        //set方法加强，设置服务器的时间,且exTime的单位是秒
        public static String setEx(String key,String value,int exTime){
            ShardedJedis jedis = null;
            String result = null;

            try {
                jedis = RedisShardedPool.getJedis();
                result = jedis.setex(key,exTime,value);
            } catch (Exception e) {
                log.error("setex key:{} value:{} error",key,value,e);
                RedisShardedPool.returnBrokenResource(jedis);
                return result;
            }
            RedisShardedPool.returnResource(jedis);
            return result;
        }

        //set
        public static String set(String key,String value){
            ShardedJedis jedis = null;
            String result = null;

            try {
                jedis = RedisShardedPool.getJedis();
                result = jedis.set(key,value);
            } catch (Exception e) {
                log.error("set key:{} value:{} error",key,value,e);
                RedisShardedPool.returnBrokenResource(jedis);
                return result;
            }
            RedisShardedPool.returnResource(jedis);
            return result;
        }

        //get
        public static String get(String key){
            ShardedJedis jedis = null;
            String result = null;

            try {
                jedis = RedisShardedPool.getJedis();
                result = jedis.get(key);
            } catch (Exception e) {
                log.error("get key:{} value:{} error",key,e);
                RedisShardedPool.returnBrokenResource(jedis);
                return result;
            }
            RedisShardedPool.returnResource(jedis);
            return result;
        }

        //删除
        public static Long del(String key){
            ShardedJedis jedis = null;
            Long result = null;

            try {
                jedis = RedisShardedPool.getJedis();
                result = jedis.del(key);
            } catch (Exception e) {
                log.error("del key:{} error",key,e);
                RedisShardedPool.returnBrokenResource(jedis);
                return result;
            }
            RedisShardedPool.returnResource(jedis);
            return result;
        }
//测试
//    public static void main(String[] args) {
//            ShardedJedis jedis = RedisShardedPool.getJedis();
//            RedisShardedPoolUtil.set("name2","yyyzzz");
//            String value = RedisShardedPoolUtil.get("name2");
//            RedisShardedPoolUtil.setEx("keyex","valueex",60*10);
//            RedisShardedPoolUtil.expire("name2",60*20);
//            RedisShardedPoolUtil.del("name2");
//            System.out.println("end");
//    }
//



}
