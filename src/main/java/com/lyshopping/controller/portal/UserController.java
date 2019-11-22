package com.lyshopping.controller.portal;

import com.lyshopping.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author liuying
 * 前端用户功能模块
 **/
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /***
     * 用户登录
     * @param password
     * @param session
     * @param username
     * */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String username, String password, HttpSession session){

        return null;
    }


}
