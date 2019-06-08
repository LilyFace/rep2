package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: YangRunTao
 * @Description: 购物车业务逻辑
 * @Date: 2019/06/04 14:36
 * @Modified By:
 */
@SuppressWarnings({"JavaDoc", "SpringJavaAutowiredMembersInspection"})
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper tbItemMapper;

    /**
     * @param cartList
     * @param itemId
     * @param num
     * @description: 添加商品到购物车
     * @return: java.util.List<com.pinyougou.pojogroup.Cart>
     * @author: YangRunTao
     * @date: 2019/06/04 14:36
     * @throws:
     **/
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
        //1.根据skuID查询商品明细SKU的对象
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        if (tbItem == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!tbItem.getStatus().equals("1")) {
            throw new RuntimeException("商品状态不合法");
        }
        //2.根据SKU对象得到商家ID
        String sellerId = tbItem.getSellerId();

        //3.根据商家ID在购物车列表中查询购物车对象(此时可能已经有该商家的商品)
        Cart cart = searchCartBySellerId(cartList, sellerId);
        //4.如果购物车列表中不存在该商家的购物车
        if (cart == null) {
            //4.1 创建一个新的购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId);
            cart.setSellerName(tbItem.getSeller());
            //创建购物车详情对象并添加到购物车详情列表,添加购物车详情列表到该购物车对象
            TbOrderItem orderItem = createOrderItem(tbItem, num);
            List<TbOrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);

            //4.2将新的购物车对象添加到购物车列表中
            cartList.add(cart);
        } else {//5.如果购物车列表中存在该商家的购物车
            // 判断该商品是否在该购物车的明细列表中存在
            TbOrderItem tbOrderItem = searchOrderItemByItemId(cart.getOrderItemList(), itemId);
            if (tbOrderItem == null) {
                //5.1如果不存在，创建新的购物车明细对象，并添加到该购物车的明细列表中
                tbOrderItem = createOrderItem(tbItem, num);
                cart.getOrderItemList().add(tbOrderItem);
            } else {
                //5.2 如果存在，在原有的数量上添加数量 ,并且更新金额
                tbOrderItem.setNum(tbOrderItem.getNum() + num);
                tbOrderItem.setTotalFee(new BigDecimal(tbOrderItem.getPrice().doubleValue() * tbOrderItem.getNum()));
                //当明细的数量小于等于0，移除此明细
                if (tbOrderItem.getNum() <= 0) {
                    cart.getOrderItemList().remove(tbOrderItem);
                }
                //当购物车的明细数量为0，在购物车列表中移除此购物车
                if (cart.getOrderItemList().size() == 0) {
                    cartList.remove(cart);
                }
            }
        }

        return cartList;
    }

    /**
     * @param cartList
     * @param sellerId
     * @description: 根据商家ID在购物车列表中查询购物车对象
     * @return: com.pinyougou.pojogroup.Cart
     * @author: YangRunTao
     * @date: 2019/06/04 14:51
     * @throws:
     **/
    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }

    /**
     * 创建购物车明细对象
     *
     * @param item
     * @param num
     * @return
     */
    private TbOrderItem createOrderItem(TbItem item, Integer num) {
        //创建新的购物车明细对象
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
        return orderItem;
    }

    /**
     * 根据skuID在购物车明细列表中查询购物车明细对象
     *
     * @param orderItemList
     * @param itemId
     * @return
     */
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem orderItem : orderItemList) {
            if (orderItem.getItemId().longValue() == itemId.longValue()) {
                return orderItem;
            }
        }
        return null;
    }
}

