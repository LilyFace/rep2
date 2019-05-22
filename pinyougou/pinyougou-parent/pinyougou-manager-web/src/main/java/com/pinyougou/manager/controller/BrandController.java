package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: 品牌控制层
 * @Date: 2019/05/07 16:49
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    /**
     * @param
     * @description: 查询所有品牌数据
     * @return: java.util.List<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/08 16:01
     * @throws:
     **/
    @RequestMapping("/findAll.do")
    public List<TbBrand> findAll() {
        //System.out.println(brandService.findAll());
        return brandService.findAll();
    }

    /**
     * @param pageNum
     * @param pageSize
     * @description: 分页查询所有品牌数据
     * @return: PageResult<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/08 16:01
     * @throws:
     **/
    @RequestMapping("/findPage.do")
    public PageResult<TbBrand> findPage(@RequestParam(name = "page") Integer pageNum, @RequestParam(name = "rows") Integer pageSize) {
        return brandService.findPage(pageNum, pageSize);
    }

    /**
     * @param tbBrand
     * @description: 添加品牌
     * @return: Result
     * @author: YangRunTao
     * @date: 2019/05/08 18:13
     * @throws:
     **/
    @RequestMapping("/addBrand.do")
    public Result addBrand(@RequestBody TbBrand tbBrand) {
        Result result;
        try {
            brandService.addBrand(tbBrand);
            result = new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, "添加失败");
        }
        return result;
    }

    /**
     * @param id
     * @description: 查询指定的品牌
     * @return: com.pinyougou.pojo.TbBrand
     * @author: YangRunTao
     * @date: 2019/05/08 20:07
     * @throws:
     **/
    @RequestMapping("/findOneBrand.do")
    public TbBrand findOneBrand(Long id) {
        return brandService.findOneBrand(id);
    }

    /**
     * @param tbBrand
     * @description: 修改品牌
     * @return: Result
     * @author: YangRunTao
     * @date: 2019/05/08 20:13
     * @throws:
     **/
    @RequestMapping("/updateBrand.do")
    public Result updateBrand(@RequestBody TbBrand tbBrand) {
        Result result;
        try {
            brandService.updateBrand(tbBrand);
            result = new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, "修改失败");
        }
        return result;
    }

    /**
     * @param
     * @description: 批量删除品牌
     * @return: Result
     * @author: YangRunTao
     * @date: 2019/05/09 9:39
     * @throws:
     **/
    @RequestMapping("/deleteBrand.do")
    public Result deleteBrand(Long[] ids) {
        Result result;
        try {
            brandService.deleteBrand(ids);
            result = new Result(true, "批量删除品牌成功");
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false, "批量删除品牌失败");
        }
        return result;
    }

    /**
     * @param tbBrand
     * @param pageNum
     * @param pageSize
     * @description: 条件分页查询所有品牌数据
     * @return: PageResult<com.pinyougou.pojo.TbBrand>
     * @author: YangRunTao
     * @date: 2019/05/09 11:52
     * @throws:
     **/
    @RequestMapping("/searchBrands.do")
    public PageResult<TbBrand> searchBrands(@RequestBody TbBrand tbBrand, @RequestParam(name = "page") Integer pageNum, @RequestParam(name = "rows") Integer pageSize) {
        return brandService.findPage(tbBrand, pageNum, pageSize);
    }

    /**
     * @param
     * @description: 查询品牌数据（id，name）
     * @return: java.util.List<java.util.Map>
     * @author: YangRunTao
     * @date: 2019/05/10 15:11
     * @throws:
     **/
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return brandService.selectOptionList();
    }

}
