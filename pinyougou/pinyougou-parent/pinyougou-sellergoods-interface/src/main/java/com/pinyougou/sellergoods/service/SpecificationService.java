package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbSpecification;

import com.pinyougou.pojogroup.Specification;
import entity.PageResult;

/**
 * 服务层接口
 *
 * @author Administrator
 */
@SuppressWarnings("ALL")
public interface SpecificationService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbSpecification> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(Specification specification);


    /**
     * 修改
     */
    public void update(Specification specification);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public Specification findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbSpecification specification, int pageNum, int pageSize);

    /**
     * @param
     * @description: 查询规格数据id，name
     * @return: java.util.List<java.util.Map>
     * @author: YangRunTao
     * @date: 2019/05/10 16:16
     * @throws:
     **/
    List<Map> selectSpecificationList();
}
