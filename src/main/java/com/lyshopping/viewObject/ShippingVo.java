package com.lyshopping.viewObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author liuying
 * 购物相关
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingVo {
    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;
}
