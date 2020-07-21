package com.lyshopping.task;

import com.lyshopping.common.Const;
//import com.lyshopping.common.RedissonManage;
import com.lyshopping.service.IOrderService;
import com.lyshopping.util.PropertiesUtil;
import com.lyshopping.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author liuying
 * 定时关单(分布式锁)
 **/
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

//    @Autowired
//    private RedissonManage redissonManage;

    /**
     * 方法v2的折中方案
     * 当我们没有使用QU进程的方式关闭tomcat的时候，tomcat会调用@PreDestroy这个注解注册的方法
     * 缺点：假如tomcat暴力关闭后，这边存在很多很多锁，此时关闭的时间会特别的长
     *      假如tomcat以Qt方式退出，该方法是不会执行的
     */
    @PreDestroy
    public void delLock(){
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

    /**
     * 单体应用下的定时关闭订单，不存在分布式问题
     * **/
    //@Scheduled(cron="0 */1 * * * ?")//每一分钟的整数倍
    public void closeOrderTaskV1(){
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }


    /**
     * SpringScheduled和redis分布式锁，实现分布式下的任务调度
     * 缺点：虽然在closeOrder方法中设置了锁的有效期，但是在程序执行setnx成功后，lock此时已经存在redis里面但是此时的lock是没有有效期的，
     * 如果此时tomcat重启还是会产生死锁，这个锁就会产生死锁，该锁是不会释放的
     */
    //@Scheduled(cron="0 */1 * * * ?")//每一分钟的整数倍
    public void closeOrderTaskV2(){
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
        Long setnxResult= RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult != null && setnxResult.intValue() == 1){
            //如果返回值为1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            log.info("没有获取到分布式锁：{}" ,Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务结束");
    }

    /**
     * springScheduled与redis原生实现分布式定时任务的改良版
     * **/
    @Scheduled(cron="0 */1 * * * ?")//每一分钟的整数倍
    public void closeOrderTaskV3(){
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout","5000"));
        Long setnxResult= RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
        if(setnxResult != null && setnxResult.intValue() == 1){
            //如果返回值为1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }else{
            //未获取到分布式锁，继续判断，判断时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if(lockValueStr != null && System.currentTimeMillis()> Long.parseLong(lockValueStr)){
                //假如一个tomcat的话，此时的lockValueStr的值与getSerResult的值应该是相等的，但是此时是tomcat集群环境，有多个进程在执行，
                // 可能lockValueStr的值就被另一个进程修改了，所以此时要获取当下的lockValueStr
               String getSerResult =  RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
               //再次用当前的时间戳
               //返回给定的key的旧值，然后根据旧值判断，是否可以获取到锁
               //当key没有旧值得时候，既key不存在，则此时返回nil，之后再去获取锁
               //这里我们set了一个新的value值，获取旧的值
                if(getSerResult == null || (getSerResult != null && StringUtils.equals(lockValueStr,getSerResult))){
                    //真正获取到锁
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }else{
                    log.info("没有获取到分布式锁：{} ",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            }else{
                log.info("没有获取到分布式锁：{} ",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
            log.info("没有获取到分布式锁：{}" ,Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务结束");
    }

    /**
     * springScheduled与redisson实现分布式定时任务
     * 因为redisson不支持分片的redis，这里当演示用
     * **/
    //@Scheduled(cron="0 */1 * * * ?")//每一分钟的整数倍
//    public void closeOrderTaskV4(){
//        RLock lock = redissonManage.getRedission().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
//        boolean getLock = false;
//        try {
//            //锁最多等待2秒，5秒后释放
//            //这边的waitTime应该设置为0，因为等待时间过长，可能导致两边同时拿到分布式锁的问题
//            // （当定时任务执行的非常快的时候，等待时间过长就会导致其他线程获取到该锁）
//            if(getLock = lock.tryLock(0,5, TimeUnit.SECONDS)){
//                log.info("Redisson获取分布式锁：{} ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//                int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
//                iOrderService.closeOrder(hour);
//            }else{
//                log.info("Redisson没有获取到分布式锁：{}，ThreadName:{}",
//                        Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
//            }
//        } catch (InterruptedException e) {
//            log.error("redisson分布式获取锁异常");
//        }finally{
//            if(!getLock){
//                return;
//            }
//            lock.unlock();
//            log.info("Redisson分布式释放锁");
//        }
//    }

    /**
     * 关闭订单并且释放锁
     */
    private void closeOrder(String lockName){
        //设置有效期5秒，防止死锁
        RedisShardedPoolUtil.expire(lockName,5);
        log.info("获取{}，ThreadName:{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour","2"));
        iOrderService.closeOrder(hour);
        //主动释放锁
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{}，ThreadName:{}" ,Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("===================");
        log.info("查看时候还有锁{}",Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }






}
