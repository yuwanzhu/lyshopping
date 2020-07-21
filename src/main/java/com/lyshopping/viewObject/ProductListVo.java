package com.lyshopping.viewObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author liuying
 * 品类管理的列表的viewObject
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListVo {
    private Integer id;
    private Integer categoryId;

    private String name;
    private String subtitle;
    private String mainImage;

    private BigDecimal price;

    private Integer status;

    private String imageHost;
}
