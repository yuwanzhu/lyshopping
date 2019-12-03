package com.lyshopping.service;

import com.github.pagehelper.PageInfo;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.Shipping;

/**
 * @author liuying
 * 购物相关接口
 **/
public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> del(Integer userId,Integer shippingId);
    ServerResponse update(Integer userId, Shipping shipping);
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);


}
