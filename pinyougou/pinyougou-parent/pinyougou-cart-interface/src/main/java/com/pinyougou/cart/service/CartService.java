package com.pinyougou.cart.service;

import com.pinyougou.pojogroup.Cart;

import java.util.List;

/**
 * @Author: YangRunTao
 * @Description: 购物车业务逻辑
 * @Date: 2019/06/04 14:32
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
public interface CartService {
    /**
     * @param cartList
     * @param itemId
     * @param num
     * @description: 添加商品到购物车
     * @return: java.util.List<com.pinyougou.pojogroup.Cart>
     * @author: YangRunTao
     * @date: 2019/06/04 14:35
     * @throws:
     **/
    List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);
}
