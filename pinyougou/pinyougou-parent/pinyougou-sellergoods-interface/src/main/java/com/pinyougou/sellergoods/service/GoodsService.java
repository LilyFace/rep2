package com.pinyougou.sellergoods.service;

import java.util.List;

import com.pinyougou.pojo.TbGoods;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import entity.PageResult;

/**
 * 服务层接口
 *
 * @author Administrator
 */
@SuppressWarnings("JavaDoc")
public interface GoodsService {

    /**
     * 返回全部列表
     *
     * @return
     */
    List<TbGoods> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    void add(TbGoods goods);


    /**
     * 修改
     */
    void update(Goods goods);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    Goods findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    PageResult findPage(TbGoods goods, int pageNum, int pageSize);

    /**
     * @param goods
     * @description: 商品基本信息录入
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/14 15:44
     * @throws:
     **/
    void add(Goods goods);

    /**
     * 批量修改状态
     *
     * @param ids
     * @param status
     */
    void updateStatus(Long[] ids, String status);

    //上下架商品
    void upAndDownGoods(Long[] ids, String isMarketableStatus);

    /**
     * @param ids
     * @param status
     * @description: 根据商品ID和状态查询Item表信息
     * @return: java.util.List<com.pinyougou.pojo.TbItem>
     * @author: YangRunTao
     * @date: 2019/05/27 14:46
     * @throws:
     **/
    List<TbItem> findItemListByIdsAndStatus(Long[] ids, String status);
}
