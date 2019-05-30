package com.pinyougou.shop.controller;

import java.util.List;

import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;
import entity.Result;

/**
 * controller
 *
 * @author Administrator
 */
@SuppressWarnings("JavaDoc")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 40000)
    private GoodsService goodsService;

    @Reference(timeout = 40000)
    private ItemSearchService itemSearchService;

    @Reference(timeout = 40000)
    private ItemPageService itemPageService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Goods goods) {
        //校验是否是当前商家的id
        Goods goods2 = goodsService.findOne(goods.getGoods().getId());
        //获取当前登录的商家ID
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //如果传递过来的商家ID并不是当前登录的用户的ID,则属于非法操作
        if (!goods2.getGoods().getSellerId().equals(sellerId) || !goods.getGoods().getSellerId().equals(sellerId)) {
            return new Result(false, "操作非法");
        }

        try {
            goodsService.update(goods);
            //删除solr库中的数据
            itemSearchService.deleteItemListInSolr(new Long[]{goods.getGoods().getId()});
            //删除对应的商品详情静态页面
            itemPageService.deleteItemHtml(goods.getGoods().getId());

            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            //删除solr库中的数据
            itemSearchService.deleteItemListInSolr(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param goods
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        //获取商家id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //添加查询条件
        goods.setSellerId(sellerId);
        return goodsService.findPage(goods, page, rows);
    }

    /**
     * @param goods
     * @description: 商品基本信息录入
     * @return: entity.Result
     * @author: YangRunTao
     * @date: 2019/05/14 15:54
     * @throws:
     **/
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        //获得商家登录名
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        TbGoods tbGoods = goods.getGoods();
        if (tbGoods == null) {
            return new Result(false, "增加失败,需要商品名");
        } else {
            tbGoods.setSellerId(sellerId);
            try {
                goodsService.add(goods);
                return new Result(true, "增加成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "增加失败");
            }
        }
    }

    /**
     * @param ids
     * @param isMarketableStatus
     * @description: 上下架商品
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/20 17:36
     * @throws:
     **/
    @RequestMapping("/upAndDownGoods")
    public Result upAndDownGoods(@RequestBody Long[] ids, String isMarketableStatus) {
        try {
            goodsService.upAndDownGoods(ids, isMarketableStatus);
            //(我认为并非运营商审核通过就立即跟新前台就显示sku信息，应该取决于商家是否上架，以后有时间修改)
            //按照SPU ID查询 SKU列表(状态为1)
            if (StringUtils.isNotEmpty(isMarketableStatus)) {
                if (isMarketableStatus.equals("1")) {
                    //查询出修改为1的数据以提供我们将其增加到solr
                    List<TbItem> tbItems = goodsService.findItemListByIdsAndStatus(ids, isMarketableStatus);
                    //将其添加到solr库
                    if (tbItems != null && tbItems.size() > 0) {
                        itemSearchService.importItemListInSolr(tbItems);
                    } else {
                        System.out.println("没有明细数据！");
                    }

                    //生成和删除静态页
                    if (ids.length > 0) {
                        //静态页生成
                        for (Long goodsId : ids) {
                            itemPageService.genItemHtml(goodsId);
                        }
                    }
                } else if (isMarketableStatus.equals("0")) {
                    //删除solr库中的数据
                    itemSearchService.deleteItemListInSolr(ids);

                    //删除静态页
                    for (Long goodsId : ids) {
                        itemPageService.deleteItemHtml(goodsId);
                    }
                }
            }

            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }

    }



    /**
     * 生成静态页（测试）
     *
     * @param goodsId
     */
    @RequestMapping("/genHtml")
    public void genHtml(Long goodsId) {
        itemPageService.genItemHtml(goodsId);
    }

}
