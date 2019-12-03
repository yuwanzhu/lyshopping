package com.lyshopping.controller.portal;

import com.lyshopping.common.Const;
import com.lyshopping.common.ResponseCode;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.User;
import com.lyshopping.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

/**
 * @author liuying
 * 门户（用户）功能模块
 **/
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /***
     * 用户登录
     * @param session
     * @param password 密码
     * @param username 用户名
     * @return status(1:密码错误; 0:成功)
     * */
    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /***
     * 退出登录
     * **/
    @RequestMapping(value = "logout.do", method = RequestMethod.GET )
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return  ServerResponse.createBySuccess();
    }

    /**
     * 注册
     * @param user user对应属性
     * @return status(1：用户已存在; 0:注册成功)
     */
    @RequestMapping(value = "register.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 校验用户名或者邮箱是否有效
     * @param str  可以是用户名，也可以是邮箱
     * @param type 对应username和email
     * @return
     */
    @RequestMapping(value = "check_valid.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String>  checkvalid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_User_Info.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }

    /**
     * 忘记密码
     * @param username 用户名
     * @return status( 0:成功； 1:失败)
     */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     *提交问题答案
     * @param username 用户名
     * @param question 问题
     * @param answer 答案
     * @return token（具有有效期）
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question, String answer){
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     *忘记密码的重新设置密码
     * @param username 用户名
     * @param passwordNew 新密码
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登录转态的重置密码
     * @param session
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @return
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld, String passwordNew){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    /**
     *登录状态更新个人信息
     * @param session
     * @param user 用户信息
     * @return
     */
    @RequestMapping(value = "update_information.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
       User currentUser =(User)session.getAttribute(Const.CURRENT_USER);
       if(currentUser == null){
           return ServerResponse.createByErrorMessage("用户未登录");
       }
       user.setId(currentUser.getId());
       user.setUsername(currentUser.getUsername());
       ServerResponse<User> response = iUserService.updateInformation(user);
       if(response.isSuccess()){
           response.getData().setUsername(currentUser.getUsername());
           session.setAttribute(Const.CURRENT_USER,response.getData());
       }
       return response;
    }

    /**
     * 获取用户的详细信息，并强制登录
     * @param session
     * @return
     */
    @RequestMapping(value = "get_information.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录;status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }

}
