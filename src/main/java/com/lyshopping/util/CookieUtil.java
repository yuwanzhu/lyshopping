package com.lyshopping.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuying
 * cookie工具类
 **/
@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMAIN = ".mall.com";
    private final static String COOKIE_NAME = "mall_login_token";

    //读cookie
    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for(Cookie ck : cks){
                log.info("return cookieName:{} cookieValue:{}" ,ck.getName(),ck.getValue());
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)) {
                    log.info("return cookieName:{} cookieValue:{}" ,ck.getName(),ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }


    //写cookie
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        //代表根目录下的所有能访问到此cookie，假如设置为“test”,则只有test开头的才能访问
        ck.setPath("/");
        ck.setHttpOnly(true);
        //单位是秒
        //如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存中。只在当前页面有效。
        //如果设置是-1，则代表永久
        ck.setMaxAge(60*60*24*365);
        log.info("write cookieName:{},cookieValue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);
    }

    //删除cookie
    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cks = request.getCookies();
        if(cks!=null){
            for(Cookie ck: cks){
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    //设置有效期为0，表示删除此cookie
                    ck.setMaxAge(0);
                    log.info("del cookieName:{} cookieValue:{}" ,ck.getName(),ck.getValue());
                    response.addCookie(ck);
                }
            }
        }
    }
}
