package com.lyshopping.controller.portal;

import com.lyshopping.common.Const;
import com.lyshopping.common.ResponseCode;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.User;
import com.lyshopping.service.IUserService;
import com.lyshopping.util.CookieUtil;
import com.lyshopping.util.JsonUtil;
import com.lyshopping.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author liuying
 * springSession演示单点登录（缺点就是不支持redis的分布式）
 **/
@Controller
@RequestMapping("/user/springsession")
public class UserSpringSessionController {

    @Autowired
    private IUserService iUserService;

    /***
     * 用户登录
     * @param session
     * @param password 密码
     * @param username 用户名
     * @return status(1:密码错误; 0:成功)
     * 返回值是把user对象放在超级可复用的ServerResponse对象中
     * */
    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        //测试springmvc全局异常
//        int i = 0;
//        int j = 666;
//        int k = j/i;

        //iUserService.login去数据库中查询对应的username与password，成功与否会将值反映到response
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()){
         session.setAttribute(Const.CURRENT_USER,response.getData());
//         CookieUtil.writeLoginToken(httpServletResponse,session.getId());
//         RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    /***
     * 退出登录
     * **/
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
       // session.removeAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        CookieUtil.delLoginToken(httpServletRequest,httpServletResponse);
        RedisShardedPoolUtil.del(loginToken);
        return  ServerResponse.createBySuccess();
    }

      /**
     * 获取用户信息
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "get_User_Info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest httpServletRequest){
       // User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }
}
