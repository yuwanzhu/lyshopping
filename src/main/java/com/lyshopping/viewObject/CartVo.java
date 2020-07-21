package com.lyshopping.viewObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuying
 * 购物车的vo
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartVo {
    private List<CartProductVo> cartProductVoList;
    /**购物车总价*/
    private BigDecimal cartTotalPrice;
    /**是否已经都勾选*/
    private Boolean allChecked;
    /**购物车图片*/
    private String imageHost;
}
