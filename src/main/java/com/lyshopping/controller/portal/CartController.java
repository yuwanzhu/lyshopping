package com.lyshopping.controller.portal;

import com.lyshopping.common.Const;
import com.lyshopping.common.ResponseCode;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.User;
import com.lyshopping.service.ICartService;
import com.lyshopping.viewObject.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    /**
     * 添加商品到购物车
     * @param session 判断登录
     * @param count  数量
     * @param productId 商品id
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(),productId,count);
    }


    /**
     * 更新购物车某个产品数量
     * @param session
     * @param count 数量
     * @param productId 商品id
     * @return
     */
        @RequestMapping("update.do")
        @ResponseBody
        public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId){
            User user = (User)session.getAttribute(Const.CURRENT_USER);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.update(user.getId(),productId,count);
        }

    /**
     * 移除购物车中某个商品
     * @param session
     * @param productIds 产品的id（可以带多个参数，用","(逗号分隔)）
     * @return
     */
        @RequestMapping("delete_product.do")
        @ResponseBody
        public ServerResponse<CartVo> deleteProduct(HttpSession session,String productIds){
            User user = (User)session.getAttribute(Const.CURRENT_USER);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.deleteProduct(user.getId(),productIds);
        }

    /**
     * 购物车全选（影响总价和是否全选对应字段的值）
     * @param session
     * @return
     */
    @RequestMapping("select_all.do")
        @ResponseBody
        public ServerResponse<CartVo> selectAll(HttpSession session){
            User user = (User)session.getAttribute(Const.CURRENT_USER);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
        }

    /**
     * 取消购物车全选
     * @param session
     * @return
     */
    @RequestMapping("un_select_all.do")
        @ResponseBody
        public ServerResponse<CartVo> unSelectAll(HttpSession session){
            User user = (User)session.getAttribute(Const.CURRENT_USER);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
        }


    /**
     * 购物车选中某个产品（总价格进行相应的计算）
     * @param session
     * @param productId 产品id
     * @return
     */
        @RequestMapping("select.do")
        @ResponseBody
        public ServerResponse<CartVo> select(HttpSession session,Integer productId){
            User user = (User)session.getAttribute(Const.CURRENT_USER);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
        }

    /**
     * 取消选中某个产品
     * @param session
     * @param productId 商品id
     * @return
     */
        @RequestMapping("un_select.do")
        @ResponseBody
        public ServerResponse<CartVo> unSelect(HttpSession session,Integer productId){
            User user = (User)session.getAttribute(Const.CURRENT_USER);
            if(user ==null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            }
            return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
        }


    /**
     * 查询购物车中产品的数量
     * @param session
     * @return
     */
    @RequestMapping("get_cart_product_count.do")
        @ResponseBody
        public ServerResponse<Integer> getCartProductCount(HttpSession session){
            User user = (User)session.getAttribute(Const.CURRENT_USER);
            if(user ==null){
                return ServerResponse.createBySuccess(0);
            }
            return iCartService.getCartProductCount(user.getId());
        }
    }
