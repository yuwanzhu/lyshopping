package com.lyshopping.controller.backend;

import com.lyshopping.common.Const;
import com.lyshopping.common.RedisShardedPool;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.User;
import com.lyshopping.service.IUserService;
import com.lyshopping.util.CookieUtil;
import com.lyshopping.util.JsonUtil;
import com.lyshopping.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author liuying
 * 后台登录模块
 **/
@Controller
@RequestMapping("/manage/user")
public class UserManageController {
        @Autowired
        private IUserService iUserService;

    /**
     * 后台管理员登录
     * @param username 用户名
     * @param password 用户密码
     * @param session
     * @return
     */
        @RequestMapping(value = "login.do", method = RequestMethod.GET)
        @ResponseBody
        public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
            ServerResponse<User> response = iUserService.login(username, password);
            if (response.isSuccess()) {
                User user = response.getData();
                if (user.getRole() == Const.Role.ROLE_ADMIN) {
                    //说明登录的是管理员
                    // session.setAttribute(Const.CURRENT_USER, user);

                   //新增redis共享cookie，session的方式
                    CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                    RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),
                            Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                    return response;
                } else {
                    return ServerResponse.createByErrorMessage("不是管理员,无法登录");
                }
            }
            return response;
        }
}
