package com.lyshopping.controller.backend;

import com.github.pagehelper.PageInfo;
import com.lyshopping.common.Const;
import com.lyshopping.common.ResponseCode;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.User;
import com.lyshopping.service.IOrderService;
import com.lyshopping.service.IUserService;
import com.lyshopping.util.CookieUtil;
import com.lyshopping.util.JsonUtil;
import com.lyshopping.util.RedisPoolUtil;
import com.lyshopping.viewObject.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author liuying
 * 后台订单模块
 **/
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    /**
     * 订单列表
     * @param httpServletRequest
     * @param pageNum 页数
     * @param pageSize 每页的数据数量
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
          if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iOrderService.manageList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 订单详情
     * @param httpServletRequest
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpServletRequest httpServletRequest, Long orderNo){

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑

            return iOrderService.manageDetail(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    /**
     * 按订单号查询
     * @param httpServletRequest
     * @param orderNo 订单号
     * @param pageNum 页数
     * @param pageSize 每页条数
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpServletRequest httpServletRequest, Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
          if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iOrderService.manageSearch(orderNo,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    /**
     * 订单发货
     * @param httpServletRequest
     * @param orderNo 订单号
     * @return status（0：发货成功; 1：发货失败）
     */
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(HttpServletRequest httpServletRequest, Long orderNo){

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");

        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iOrderService.manageSendGoods(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

}

