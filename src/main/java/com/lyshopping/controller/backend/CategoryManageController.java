package com.lyshopping.controller.backend;
import com.lyshopping.common.Const;
import com.lyshopping.common.ResponseCode;
import com.lyshopping.common.ServerResponse;
import com.lyshopping.pojo.User;
import com.lyshopping.service.ICategoryService;
import com.lyshopping.service.IUserService;
import com.lyshopping.util.CookieUtil;
import com.lyshopping.util.JsonUtil;
//import com.lyshopping.util.RedisPoolUtil;
import com.lyshopping.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * @author liuying
 * 后台分类管理模块
 **/
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {


    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 增加节点
     * @param httpServletRequest
     * @param categoryName 新节点的名称
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest httpServletRequest, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
//   用户登录状态以及权限校验，先阶段全部迁移到过滤器里面
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr,User.class);
//          if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
//        }
//        //校验一下是否是管理员
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //是管理员
//            //增加我们处理分类的逻辑
//            return iCategoryService.addCategory(categoryName,parentId);
//
//        }else{
//            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
//        }
        //全部通过拦截器验证是否登录以及用户权限
        return iCategoryService.addCategory(categoryName,parentId);
    }

    /**
     * 修改品类的名字
     * @param httpServletRequest
     * @param categoryId 节点id
     * @param categoryName 品类的名称
     * @return
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest httpServletRequest,Integer categoryId,String categoryName){
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr,User.class);
//        if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //更新categoryName
//            return iCategoryService.updateCategoryName(categoryId,categoryName);
//        }else{
//            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
//        }
        return iCategoryService.updateCategoryName(categoryId,categoryName);
    }

    /**
     * 获取平级的品类子节点
     * @param httpServletRequest
     * @param categoryId（default=0） 节点id
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr,User.class);
//          if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //查询子节点的category信息,并且不递归,保持平级
//            return iCategoryService.getChildrenParallelCategory(categoryId);
//        }else{
//            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
//        }
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 获取当前分类的id及递归子节点cateGoryId
     * @param httpServletRequest
     * @param categoryId 节点id
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest httpServletRequest,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
//        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorMessage("用户未登录，无法获取用户信息");
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr,User.class);
//        if(user == null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //查询当前节点的id和递归子节点的id
////            0->10000->100000
//            return iCategoryService.selectCategoryAndChildrenById(categoryId);
//
//        }else{
//            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
//        }
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }

}