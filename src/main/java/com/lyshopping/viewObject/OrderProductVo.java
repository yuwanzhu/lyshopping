package com.lyshopping.viewObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuying
 * 订单相关
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductVo {
    private List<OrderItemVo> orderItemVoList;
    private BigDecimal productTotalPrice;
    private String imageHost;
}
