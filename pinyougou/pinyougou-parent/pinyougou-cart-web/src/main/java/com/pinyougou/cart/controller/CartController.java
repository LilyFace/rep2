package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;
import entity.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: YangRunTao
 * @Description: 购物车控制层
 * @Date: 2019/06/05 9:24
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Reference(timeout = 6000)
    private CartService cartService;

    /**
     * @param
     * @description: 从cookie中获取购物车列表
     * @return: java.util.List<com.pinyougou.pojogroup.Cart>
     * @author: YangRunTao
     * @date: 2019/06/05 14:46
     * @throws:
     **/
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        //1.调用工具类获得cookie中个购物车列表
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        //2.转换json字符串为对象
        if (StringUtils.isEmpty(cartListString)) {
            cartListString = "[]";
        }
        return JSON.parseArray(cartListString, Cart.class);
    }

    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        try {
            //1.获得cookie中的购物车列表对象
            List<Cart> cartList = findCartList();
            //2.调用服务将本次要添加的商品添加到购物车列表中
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            String cartListString = JSON.toJSONString(cartList);
            //3.重新将购物车列表对象写回到cookie
            CookieUtil.setCookie(request, response, "cartList", cartListString, 3600 * 24, "UTF-8");
            return new Result(true, "添加购物车成功");
        } catch (Exception e) {
            return new Result(false, "添加购物车失败");
        }
    }
}
