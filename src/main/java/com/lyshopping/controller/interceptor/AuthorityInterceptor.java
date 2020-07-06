package com.lyshopping.controller.interceptor;
import com.google.common.collect.Maps;
import com.lyshopping.common.Const;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.User;
import com.lyshopping.util.CookieUtil;
import com.lyshopping.util.JsonUtil;
import com.lyshopping.util.RedisShardedPoolUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author liuying
 * 拦截器
 **/
@Slf4j
public class AuthorityInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("preHandle");
        //请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod)o;

        //解析HandlerMethod
        String methodName =  handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数，具体的参数key以及value是什么，打印日志
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String mapkey = (String)entry.getKey();
            String mapValue = StringUtils.EMPTY;
            //httpServletRequest这个参数的map，里面的value返回的是一个String[]
            Object obj = entry.getValue();
            if(obj instanceof  String[]){
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapkey).append("=").append(mapkey);
        }
        //拦截器开发请求方法2
        if(StringUtils.equals(className,"UserManageController") && StringUtils.equals(methodName,"login")){
            log.info("权限拦截器拦截到请求,className:{},methodName:{}",className,methodName);
            //如果拦截到登录请求，则不打印参数因为参数里面有密码，全部打印到日志中，防止日志泄露
            //把请求转回到controller
            return true;
        }
        log.info("权限拦截器拦截到请求,className:{},methodName:{},param:{}",className,methodName,requestParamBuffer.toString());

        //用户登录以及权限判断
        User user = null;

        //获取用户登录的信息
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        //假如用户信息不为空
        if(StringUtils.isNotEmpty(loginToken)){
            //去redis里面获取用户信息
           String userJsonStr = RedisShardedPoolUtil.get(loginToken);
           user = JsonUtil.String2Obj(userJsonStr,User.class);
        }

        //当用户信息为空，或者用户的权限不是管理员
        if(user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            //返回false，即不会调用controller方法
            //这里要添加reset，否则报异常getWrite（） has already been called for this response
            httpServletResponse.reset();
            //乱码处理
            httpServletResponse.setCharacterEncoding("UTF-8");
            //这里要设置返回值的类型，因为全部都是json接口
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();

            //上传由于富文本的控件要求，要特殊处理返回值，这里面区分用户是否登录以及是否有权限
            if(user == null){
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtextImgUpload") ){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                }else{
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
              }else{
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                }else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
                }
            }
            out.flush();
            out.close();//这里需要关闭
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
