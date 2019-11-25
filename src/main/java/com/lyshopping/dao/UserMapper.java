package com.lyshopping.dao;

import com.lyshopping.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 校验用户名是否存在
     * @param username
     * @return  int
     * **/
    int checkUsername(String username);

    /**
     * 查询用户的登陆登录信息
     * @param username
     * @param password
     * @return  User
     * **/
    User selectLogin(@Param("username") String username,@Param("password") String password);

    /**
     * 校验邮箱
     * **/
    int checkEmail(String email);

    /**
     *
     * **/
     String selectQuestionByUsername(String username);

     int checkAnswer(@Param("username")String username,@Param("question") String question,@Param("answer") String answer);

     int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);

     int checkPassword(@Param(value = "password")String password,@Param("userId") Integer userId);

     int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);
}