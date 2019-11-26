package com.lyshopping.service;

import com.github.pagehelper.PageInfo;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.Product;
import com.lyshopping.viewObject.ProductDetailVo;

/**
 * @author liuying
 * 产品管理接口
 **/
public interface IProductService {
    /**
     *保存或者更新产品
     * @param product
     * @return
     * */
    ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 产品上下架
     * @param productId
     * @param status
     * @return
     * */
    ServerResponse<String> setSaleStatus(Integer productId,Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);


}
