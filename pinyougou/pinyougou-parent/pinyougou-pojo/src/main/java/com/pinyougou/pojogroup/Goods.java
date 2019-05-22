package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: YangRunTao
 * @Description: 创建组合实体类goods
 * @Date: 2019/05/14 15:39
 * @Modified By:
 */
public class Goods implements Serializable {

    private static final long serialVersionUID = 2685106274299393740L;
    private TbGoods goods;//商品SPU
    private TbGoodsDesc goodsDesc;//商品扩展
    private List<TbItem> itemList;//商品SKU列表

    public Goods() {
    }

    public Goods(TbGoods goods, TbGoodsDesc goodsDesc, List<TbItem> itemList) {
        this.goods = goods;
        this.goodsDesc = goodsDesc;
        this.itemList = itemList;
    }

    public TbGoods getGoods() {
        return goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
