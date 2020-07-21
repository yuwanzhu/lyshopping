//package com.lyshopping.common;
//import com.lyshopping.util.PropertiesUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.redisson.config.Config;
//import org.redisson.Redisson;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author liuying
// * Redisson，不支持分片的redis
// **/
//@Component
//@Slf4j
//public class RedissonManage {
//    private Config config = new Config();
//    private Redisson redission = null;
//
//    public Redisson getRedission(){
//        return redission;
//    }
//    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
//    private static Integer redis1Port =Integer.parseInt(PropertiesUtil.getProperty("redis1.port")) ;
//    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
//    private static Integer redis2Port =Integer.parseInt(PropertiesUtil.getProperty("redis2.port")) ;
//
//    /**
//     * @PostConstruct:在构造器完成之后加载
//     */
//    @PostConstruct
//    public void init(){
//        try {
//            config.useSingleServer().setAddress(
//                    new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString());
//            redission = (Redisson) Redisson.create(config);
//            log.info("初始化redisson结束");
//        } catch (Exception e) {
//           log.error("redisson init error",e);
//        }
//    }
//}
