package com.pinyougou.sellergoods.service;

import entity.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: 品牌服务接口
 * @Date: 2019/05/07 16:14
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
public interface BrandService {
    /**
     * @param
     * @description: 返回所有品牌
     * @return: java.util.List<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/07 16:15
     * @throws:
     **/
    List<TbBrand> findAll();

    /**
     * @param pageNum
     * @param pageSize
     * @description: 分页查询所有品牌
     * @return: PageResult<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/08 16:02
     * @throws:
     **/
    PageResult<TbBrand> findPage(Integer pageNum, Integer pageSize);

    /**
     * @param tbBrand
     * @param pageNum
     * @param pageSize
     * @description: 条件分页查询所有品牌
     * @return: PageResult<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/09 11:18
     * @throws:
     **/
    PageResult<TbBrand> findPage(TbBrand tbBrand, Integer pageNum, Integer pageSize);

    /**
     * @param tbBrand
     * @description: 增加品牌
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/08 18:03
     * @throws:
     **/
    void addBrand(TbBrand tbBrand);

    /**
     * @param tbBrand
     * @description: 修改品牌
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/08 19:59
     * @throws:
     **/
    void updateBrand(TbBrand tbBrand);

    /**
     * @param id
     * @description: 根据id查询品牌
     * @return: com.pinyougou.pojo.TbBrand
     * @author: YangRunTao
     * @date: 2019/05/08 20:02
     * @throws:
     **/
    TbBrand findOneBrand(Long id);

    /**
     * @param ids
     * @description: 批量删除品牌
     * @return: Result
     * @author: YangRunTao
     * @date: 2019/05/09 9:34
     * @throws:
     **/
    void deleteBrand(Long[] ids);

    /**
     * @param
     * @description: 获得品牌数据
     * @return: java.util.List<java.util.Map>
     * @author: YangRunTao
     * @date: 2019/05/10 15:08
     * @throws:
     **/
    List<Map> selectOptionList();
}
