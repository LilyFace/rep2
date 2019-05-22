package com.pinyougou.shop.controller;

import java.util.List;

import com.pinyougou.pojogroup.Goods;
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

    @Reference
    private GoodsService goodsService;

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
            goodsService.upAndDownGoods(ids,isMarketableStatus);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }
}
