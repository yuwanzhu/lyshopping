package com.lyshopping.controller.portal;

import com.lyshopping.common.Const;
import com.lyshopping.common.ResponseCode;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.User;
import com.lyshopping.service.ICartService;
import com.lyshopping.util.CookieUtil;
import com.lyshopping.util.JsonUtil;
import com.lyshopping.util.RedisPoolUtil;
import com.lyshopping.viewObject.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author liuying
 * 门户购物车
 **/
@Controller
@RequestMapping("/cart/")
public class CartController {


    @Autowired
    private ICartService iCartService;

    /**
     *购物车list列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
          if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    /**
     * 添加商品到购物车
     * @param httpServletRequest 判断登录
     * @param count  数量
     * @param productId 商品id
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpServletRequest httpServletRequest, Integer count, Integer productId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);
    }


    /**
     * 更新购物车某个产品数量
     * @param httpServletRequest
     * @param count 数量
     * @param productId 商品id
     * @return
     */
        @RequestMapping("update.do")
        @ResponseBody
        public ServerResponse<CartVo> update(HttpServletRequest httpServletRequest, Integer count, Integer productId){
            String loginToken = CookieUtil.readLoginToken(httpServletRequest);
            if(StringUtils.isEmpty(loginToken)){
                return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
            }
            String userJsonStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr,User.class);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.update(user.getId(),productId,count);
        }

    /**
     * 移除购物车中某个商品
     * @param httpServletRequest
     * @param productIds 产品的id（可以带多个参数，用","(逗号分隔)）
     * @return
     */
        @RequestMapping("delete_product.do")
        @ResponseBody
        public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest,String productIds){
            String loginToken = CookieUtil.readLoginToken(httpServletRequest);
            if(StringUtils.isEmpty(loginToken)){
                return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
            }
            String userJsonStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr,User.class);
               if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.deleteProduct(user.getId(),productIds);
        }

    /**
     * 购物车全选（影响总价和是否全选对应字段的值）
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("select_all.do")
        @ResponseBody
        public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
             if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
        }

    /**
     * 取消购物车全选
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("un_select_all.do")
        @ResponseBody
        public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
             if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
        }


    /**
     * 购物车选中某个产品（总价格进行相应的计算）
     * @param httpServletRequest
     * @param productId 产品id
     * @return
     */
        @RequestMapping("select.do")
        @ResponseBody
        public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest,Integer productId){
            String loginToken = CookieUtil.readLoginToken(httpServletRequest);
            if(StringUtils.isEmpty(loginToken)){
                return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
            }
            String userJsonStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr,User.class);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
        }

    /**
     * 取消选中某个产品
     * @param httpServletRequest
     * @param productId 商品id
     * @return
     */
        @RequestMapping("un_select.do")
        @ResponseBody
        public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest,Integer productId){
            String loginToken = CookieUtil.readLoginToken(httpServletRequest);
            if(StringUtils.isEmpty(loginToken)){
                return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
            }
            String userJsonStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr,User.class);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
        }


    /**
     * 查询购物车中产品的数量
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("get_cart_product_count.do")
        @ResponseBody
        public ServerResponse<Integer> getCartProductCount(HttpServletRequest  httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr,User.class);
            if(user ==null){
                return ServerResponse.createBySuccess(0);
            }
            return iCartService.getCartProductCount(user.getId());
        }
    }
