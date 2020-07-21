package com.lyshopping.viewObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author liuying
 * 订单相关
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemVo {
    private Long orderNo;

    private Integer productId;

    private String productName;
    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String createTime;
}
