package com.lyshopping.service;

import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.Category;

import java.util.List;

/**
 * @author liuying
 * 分类功能接口
 **/
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

}
