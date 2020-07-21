package com.lyshopping.common;

import com.lyshopping.pojo.User;
import com.lyshopping.util.CookieUtil;
import com.lyshopping.util.JsonUtil;
//import com.lyshopping.util.RedisPoolUtil;
import com.lyshopping.util.RedisShardedPoolUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author liuying
 * 重置session的过期时间
 **/
public class SessionExpireFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        if(StringUtils.isNotEmpty(loginToken)){
            //判断logintoken是否为空或者为" ";
            //如果不为空，继续拿用户信息
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr,User.class);
            if(user != null){
                //如果user不为空，则重置session的时间，即调用expire命令
                RedisShardedPoolUtil.expire(loginToken,Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
