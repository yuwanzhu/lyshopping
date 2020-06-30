package com.lyshopping.service.impl;

import com.lyshopping.common.Const;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.dao.UserMapper;
import com.lyshopping.pojo.User;
import com.lyshopping.service.IUserService;
import com.lyshopping.util.MD5Util;
import com.lyshopping.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author liuying
 * IUserService接口实现
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 登录功能
     * */
    @Override
    public ServerResponse<User> login(String username, String password) {
        System.out.println("请求到了这里："+username);
        int resultCount = userMapper.checkUsername(username);
        System.out.println("数据库中查到的数据为："+resultCount);
        if(resultCount == 0){
            return  ServerResponse.createByErrorMessage("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        System.out.println("密码为："+password + " "+md5Password);
        User user = userMapper.selectLogin(username, md5Password);
        System.out.println("+++++++"+user.toString());
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    /**
     * 用户的注册
     */
    @Override
    public ServerResponse<String> register(User user){
        //先判断用户输入的用户名是否存在
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        //int resultCount = userMapper.checkUsername(user.getUsername());
        //if(resultCount > 0){
        //    return ServerResponse.createByErrorMessage("用户名已存在");
        //}
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        //再验证邮箱是否已经使用
        //resultCount = userMapper.checkEmail(user.getEmail());
        //if(resultCount > 0){
        //    return ServerResponse.createByErrorMessage("邮箱已存在");
        //}

        //给新用户角色
        user.setRole(Const.Role.ROLE_CURRENT);

        //加密密码
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /***
     * 校验用户名或邮箱是否有限
     * **/
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        //判断type的值是否为" ";
        //StringUtils.isNotBlank(" ")的值为false
        if(StringUtils.isNotBlank(type)){
           //校验用户名
           if(Const.USERNAME.equals(type)){
               int resultCount = userMapper.checkUsername(str);
               if(resultCount > 0){
                   return ServerResponse.createByErrorMessage("用户名已存在");
               }
           }
           //校验邮箱
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
    /**
     * 查询注册时用户设置问题
     * **/
    @Override
    public ServerResponse selectQuestion(String username) {
        ServerResponse vaildResponse = this.checkValid(username,Const.USERNAME);
        if(vaildResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    /**
     * 校验问题的答案
     * **/
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0){
            //说明问题及问题答案是这个用户的，并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            //一期代碼，將token放入guavaCache中
            //TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            RedisPoolUtil.setEx(Const.TOKEN_PREFIX+username,forgetToken,60*60*12);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token  = RedisPoolUtil.get(Const.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if(StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功呢");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户，因为我们会查询一个count(1),如果不指定id，那么结果就是turn，count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        //username是不能被更新的
        //email也要进行一个校验，校验新的email是不是已经存在，并且存在的email如果相同的话，不能是我们当前的这个用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("email已存在，请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
       User user = userMapper.selectByPrimaryKey(userId);
       if(user == null){
           return ServerResponse.createByErrorMessage("找不到当前的用户");
       }
       user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    //backend
    /**
     * 校验是否是管理员
     *
     * **/
    @Override
    public ServerResponse checkAdminRole(User user) {
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    /**
     * 查询当前店铺的所有会员信息
     * */
    @Override
    public ServerResponse selectAllUser(User user) {
        user = userMapper.selectAllUsers(user);
        return ServerResponse.createBySuccess();
    }


}
