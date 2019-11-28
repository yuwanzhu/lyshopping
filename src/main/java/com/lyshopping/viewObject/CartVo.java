package com.lyshopping.viewObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuying
 * 购物车的vo
 **/
public class CartVo {
    private List<CartProductVo> cartProductVoList;
    /**购物车总价*/
    private BigDecimal cartTotalPrice;
    /**是否已经都勾选*/
    private Boolean allChecked;
    /**购物车图片*/
    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

}
