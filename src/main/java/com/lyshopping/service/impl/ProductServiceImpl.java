package com.lyshopping.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import com.lyshopping.common.Const;
import com.lyshopping.common.ResponseCode;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.dao.CategoryMapper;
import com.lyshopping.dao.ProductMapper;
import com.lyshopping.pojo.Category;
import com.lyshopping.pojo.Product;
import com.lyshopping.service.ICategoryService;
import com.lyshopping.service.IProductService;
import com.lyshopping.util.DateTimeUtil;
import com.lyshopping.util.PropertiesUtil;
import com.lyshopping.viewObject.ProductDetailVo;
import com.lyshopping.viewObject.ProductListVo;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * @author liuying
 * 商品相关功能的接口实现
 **/
@Service("iProductService")
public class ProductServiceImpl implements IProductService {
        @Autowired
        private ProductMapper productMapper;

        @Autowired
        private CategoryMapper categoryMapper;

        @Autowired
        private ICategoryService iCategoryService;

        /**
         * 跟新或者新增商品
         */
        @Override
        public ServerResponse saveOrUpdateProduct(Product product){
            if(product != null)
            {
                if(StringUtils.isNotBlank(product.getSubImages())){
                    String[] subImageArray = product.getSubImages().split(",");
                    if(subImageArray.length > 0){
                        product.setMainImage(subImageArray[0]);
                    }
                }

                if(product.getId() != null){
                    int rowCount = productMapper.updateByPrimaryKey(product);
                    if(rowCount > 0){
                        return ServerResponse.createBySuccess("更新产品成功");
                    }
                    return ServerResponse.createBySuccess("更新产品失败");
                }else{
                    int rowCount = productMapper.insert(product);
                    if(rowCount > 0){
                        return ServerResponse.createBySuccess("新增产品成功");
                    }
                    return ServerResponse.createBySuccess("新增产品失败");
                }
            }
            return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
        }

        /**
         * 产品状态修改
         */

        @Override
        public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
            if(productId == null || status == null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            Product product = new Product();
            product.setId(productId);
            product.setStatus(status);
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if(rowCount > 0){
                return ServerResponse.createBySuccess("修改产品销售状态成功");
            }
            return ServerResponse.createByErrorMessage("修改产品销售状态失败");
        }

        /***
         * 根据商品的id查询商品
         * @param productId
         * @return
         */
        @Override
        public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
            if(productId == null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            Product product = productMapper.selectByPrimaryKey(productId);
            if(product == null){
                return ServerResponse.createByErrorMessage("产品已下架或者删除");
            }
            //设置商品的属性值
            ProductDetailVo productDetailVo = assembleProductDetailVo(product);
            return ServerResponse.createBySuccess(productDetailVo);
        }
        /***
         * 根据商品的信息给商品的vo对象赋值
         */
        private ProductDetailVo assembleProductDetailVo(Product product){
            ProductDetailVo productDetailVo = new ProductDetailVo();
            productDetailVo.setId(product.getId());
            productDetailVo.setSubtitle(product.getSubtitle());
            productDetailVo.setPrice(product.getPrice());
            productDetailVo.setMainImage(product.getMainImage());
            productDetailVo.setSubImages(product.getSubImages());
            productDetailVo.setCategoryId(product.getCategoryId());
            productDetailVo.setDetail(product.getDetail());
            productDetailVo.setName(product.getName());
            productDetailVo.setStatus(product.getStatus());
            productDetailVo.setStock(product.getStock());

            productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

            Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if(category == null){
                //默认根节点
                productDetailVo.setParentCategoryId(0);
            }else{
                productDetailVo.setParentCategoryId(category.getParentId());
            }

            productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
            productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
            return productDetailVo;
        }


        /***
         * 获取商品列表(分页)
         *  startPage--start
         * 填充自己的sql查询逻辑
         * pageHelper-收尾
         * @param pageNum
         * @param pageSize
         * @return
         */
        @Override
        public ServerResponse<PageInfo> getProductList(int pageNum,int pageSize){
            PageHelper.startPage(pageNum,pageSize);
            List<Product> productList = productMapper.selectList();
            List<ProductListVo> productListVoList = Lists.newArrayList();
            for(Product productItem : productList){
                ProductListVo productListVo = assembleProductListVo(productItem);
                productListVoList.add(productListVo);
            }
            PageInfo pageResult = new PageInfo(productList);
            pageResult.setList(productListVoList);
            return ServerResponse.createBySuccess(pageResult);
        }

        /***
         * 根据商品信息，给商品列表的vo赋值
         * @param product
         * @return
         */
        private ProductListVo assembleProductListVo(Product product){
            ProductListVo productListVo = new ProductListVo();
            productListVo.setId(product.getId());
            productListVo.setName(product.getName());
            productListVo.setCategoryId(product.getCategoryId());
            productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
            productListVo.setMainImage(product.getMainImage());
            productListVo.setPrice(product.getPrice());
            productListVo.setSubtitle(product.getSubtitle());
            productListVo.setStatus(product.getStatus());
            return productListVo;
        }

        /***
         * 商品查询（分页）
         * @param productName
         * @param productId
         * @param pageNum
         * @param pageSize
         * @return
         */
        @Override
        public ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize){
            PageHelper.startPage(pageNum,pageSize);
            if(StringUtils.isNotBlank(productName)){
                productName = new StringBuilder().append("%").append(productName).append("%").toString();
            }
            List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
            List<ProductListVo> productListVoList = Lists.newArrayList();
            for(Product productItem : productList){
                ProductListVo productListVo = assembleProductListVo(productItem);
                productListVoList.add(productListVo);
            }
            PageInfo pageResult = new PageInfo(productList);
            pageResult.setList(productListVoList);
            return ServerResponse.createBySuccess(pageResult);
        }

    /**
     * 通过id查询商品信息（供给用户端调用的方法，多了一个状态判断的操作）
     * @param productId
     * @return
     */
    @Override
        public ServerResponse<ProductDetailVo> getProductDetail(Integer productId){
            if(productId == null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            Product product = productMapper.selectByPrimaryKey(productId);
            if(product == null){
                return ServerResponse.createByErrorMessage("产品已下架或者删除");
            }
            //判断商品的状态是否为ture
            if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
                return ServerResponse.createByErrorMessage("产品已下架或者删除");
            }
            ProductDetailVo productDetailVo = assembleProductDetailVo(product);
            return ServerResponse.createBySuccess(productDetailVo);
        }

    /***
     * 查询商品列表
     * @param keyword
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
        @Override
        public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
            if(StringUtils.isBlank(keyword) && categoryId == null){
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
            }
            List<Integer> categoryIdList = new ArrayList<Integer>();

            if(categoryId != null){
                Category category = categoryMapper.selectByPrimaryKey(categoryId);
                if(category == null && StringUtils.isBlank(keyword)){
                    //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                    PageHelper.startPage(pageNum,pageSize);
                    List<ProductListVo> productListVoList = Lists.newArrayList();
                    PageInfo pageInfo = new PageInfo(productListVoList);
                    return ServerResponse.createBySuccess(pageInfo);
                }
                categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
            }
            if(StringUtils.isNotBlank(keyword)){
                keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
            }

            PageHelper.startPage(pageNum,pageSize);
            //排序处理
            if(StringUtils.isNotBlank(orderBy)){
                if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                    String[] orderByArray = orderBy.split("_");
                    PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
                }
            }
            List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);

            List<ProductListVo> productListVoList = Lists.newArrayList();
            for(Product product : productList){
                ProductListVo productListVo = assembleProductListVo(product);
                productListVoList.add(productListVo);
            }

            PageInfo pageInfo = new PageInfo(productList);
            pageInfo.setList(productListVoList);
            return ServerResponse.createBySuccess(pageInfo);
        }

}
