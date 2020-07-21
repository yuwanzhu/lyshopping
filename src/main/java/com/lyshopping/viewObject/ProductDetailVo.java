package com.lyshopping.viewObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author liuying
 * 商品相关
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailVo {

    private Integer  id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String createTime;
    private String updateTime;


    private String imageHost;
    private Integer parentCategoryId;

}
