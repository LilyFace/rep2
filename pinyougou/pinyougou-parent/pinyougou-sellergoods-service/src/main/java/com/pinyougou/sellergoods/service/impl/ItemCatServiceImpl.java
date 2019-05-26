package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.pojo.TbItemCatExample.Criteria;
import com.pinyougou.sellergoods.service.ItemCatService;

import entity.PageResult;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@SuppressWarnings({"JavaDoc", "SpringJavaAutowiredMembersInspection"})
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    private Boolean flag = true;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(null);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbItemCat itemCat) {
        //重新写入缓存,需要标记为true
        flag = true;
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbItemCat itemCat) {
        //重新写入缓存,需要标记为true
        flag = true;
        itemCatMapper.updateByPrimaryKey(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        Criteria criteria = tbItemCatExample.createCriteria();
        for (Long id : ids) {
            criteria.andParentIdEqualTo(id);
            //查询是否有子节点
            List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(tbItemCatExample);
            if (tbItemCats == null || tbItemCats.size() == 0) {
                itemCatMapper.deleteByPrimaryKey(id);
            } else {
                throw new RuntimeException("有儿子");
            }

        }
        //重新写入缓存,需要标记为true
        flag = true;
    }


    @Override
    public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();

        if (itemCat != null) {
            if (itemCat.getName() != null && itemCat.getName().length() > 0) {
                criteria.andNameLike("%" + itemCat.getName() + "%");
            }

        }

        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(example);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * @param parentId
     * @description: 根据父id查询
     * @return: java.util.List<com.pinyougou.pojo.TbItemCat>
     * @author: YangRunTao
     * @date: 2019/05/13 20:22
     * @throws:
     **/
    @Override
    public List<TbItemCat> findByParentId(Long parentId) {
        //条件查询的条件
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);


        //将数据存入缓存提供前台使用(每次执行查询的时候，一次性读取缓存进行存储 (因为每次增删改都要执行此方法))
        //调用标记类,通过标记判定是否需要存入到缓存
        //(如果是查询就默认写入缓存一次，如果是曾删改带来的查询，就在对应的增删改带来的方法中设置flag来重写缓存)
        if (flag) {
            List<TbItemCat> list = findAll();
            for (TbItemCat tbItemCat : list) {
                @SuppressWarnings("unchecked")
                BoundHashOperations itemCat = redisTemplate.boundHashOps("itemCat");
                //noinspection unchecked
                itemCat.put(tbItemCat.getName(), tbItemCat.getTypeId());
            }
            System.out.println("更新缓存:商品分类表");
            flag = false;
        }


        return itemCatMapper.selectByExample(tbItemCatExample);
    }
}
