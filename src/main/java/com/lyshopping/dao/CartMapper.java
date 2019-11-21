package com.lyshopping.dao;

import com.lyshopping.pojo.Cart;

/**
 * @author 刘颖
 * 购物车接口
 * **/
public interface CartMapper {

    /**
     * 通过主键删除
     * @param id
     * @return int
     * */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入购物车
     * @param record
     * @return int
     * */
    int insert(Cart record);

    /**
     * 插入购物车(有字段为null判断)
     * @param record
     * @return int
     * */
    int insertSelective(Cart record);

    /**
     * 根据主键查询对象
     * @param id
     * @return Cart
     * */
    Cart selectByPrimaryKey(Integer id);
    /**
     * 根据主键更新
     * @param record
     * @return int
     * */
    int updateByPrimaryKeySelective(Cart record);

    /**
     * 根据主键更新(有字段为null判断)
     * @param record
     * @return int
     * */
    int updateByPrimaryKey(Cart record);

}