package com.lyshopping.viewObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author liuying
 * 购物车商品的vo
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductVo {
//结合了产品和购物车的一个抽象对象

    private Integer id;
    private Integer userId;
    private Integer productId;
    /**购物车中此商品的数量*/
    private Integer quantity;
    private String productName;
    private String productSubtitle;
    /**产品的主图*/
    private String productMainImage;
    /**产品的价格*/
    private BigDecimal productPrice;
    /**产品的状态*/
    private Integer productStatus;
    /**总价*/
    private BigDecimal productTotalPrice;
    /**库存*/
    private Integer productStock;
    /**此商品是否勾选*/
    private Integer productChecked;

    /**限制数量的一个返回结果*/
    private String limitQuantity;

}
