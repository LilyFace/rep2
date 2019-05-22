package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: 品牌服务
 * @Date: 2019/05/07 16:16
 * @Modified By:
 */
@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "JavaDoc", "unused"})
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper tbBrandMapper;

    /**
     * @param
     * @description: 返回所有品牌
     * @return: java.util.List<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/08 16:03
     * @throws:
     **/
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @description: 分页查询所有品牌
     * @return: PageResult<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/08 16:03
     * @throws:
     **/
    @Override
    public PageResult<TbBrand> findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * @param tbBrand
     * @param pageNum
     * @param pageSize
     * @description: 条件分页查询所有品牌
     * @return: PageResult<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/09 11:19
     * @throws:
     **/
    @Override
    public PageResult<TbBrand> findPage(TbBrand tbBrand, Integer pageNum, Integer pageSize) {
        //分页助手对象
        PageHelper.startPage(pageNum, pageSize);
        //获得条件查询对象
        TbBrandExample tbBrandExample = new TbBrandExample();
        TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();
        //封装条件
        if (tbBrand != null) {
            //品牌名模糊查询
            if (StringUtils.isNotBlank(tbBrand.getName())) {
                criteria.andNameLike("%" + tbBrand.getName() + "%");
            }
            //首字母条件查询
            if (StringUtils.isNotBlank(tbBrand.getFirstChar())) {
                String toUpperCase = tbBrand.getFirstChar().toUpperCase();
                criteria.andFirstCharEqualTo(toUpperCase);
            }
        }
        Page<TbBrand> pages = (Page<TbBrand>) tbBrandMapper.selectByExample(tbBrandExample);
        return new PageResult<>(pages.getTotal(), pages.getResult());
    }

    /**
     * @param tbBrand
     * @description: 增加品牌
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/08 18:04
     * @throws:
     **/
    @Override
    public void addBrand(TbBrand tbBrand) {
        tbBrandMapper.insert(tbBrand);
    }

    /**
     * @param tbBrand
     * @description: 修改品牌(根据id)
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/08 20:00
     * @throws:
     **/
    @Override
    public void updateBrand(TbBrand tbBrand) {
        tbBrandMapper.updateByPrimaryKey(tbBrand);
    }

    /**
     * @param id
     * @description: 根据id查询品牌
     * @return: com.pinyougou.pojo.TbBrand
     * @author: YangRunTao
     * @date: 2019/05/08 20:03
     * @throws:
     **/
    @Override
    public TbBrand findOneBrand(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    /**
     * @param ids
     * @description: 批量删除品牌
     * @return: Result
     * @author: YangRunTao
     * @date: 2019/05/09 9:35
     * @throws:
     **/
    @Override
    public void deleteBrand(Long[] ids) {
        for (Long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return tbBrandMapper.selectOptionList();
    }

}
