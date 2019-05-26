package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbTypeTemplateMapper;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.pojo.TbTypeTemplateExample;
import com.pinyougou.pojo.TbTypeTemplateExample.Criteria;
import com.pinyougou.sellergoods.service.TypeTemplateService;

import entity.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "unchecked", "JavaDoc"})
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;

    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private Boolean flag = true;

    /**
     * 查询全部
     */
    @Override
    public List<TbTypeTemplate> findAll() {
        return typeTemplateMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbTypeTemplate typeTemplate) {
        flag = true;
        typeTemplateMapper.insert(typeTemplate);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbTypeTemplate typeTemplate) {
        flag = true;
        typeTemplateMapper.updateByPrimaryKey(typeTemplate);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbTypeTemplate findOne(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            typeTemplateMapper.deleteByPrimaryKey(id);
        }
        flag = true;
    }


    @Override
    public PageResult findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbTypeTemplateExample example = new TbTypeTemplateExample();
        Criteria criteria = example.createCriteria();

        if (typeTemplate != null) {
            if (typeTemplate.getName() != null && typeTemplate.getName().length() > 0) {
                criteria.andNameLike("%" + typeTemplate.getName() + "%");
            }
            if (typeTemplate.getSpecIds() != null && typeTemplate.getSpecIds().length() > 0) {
                criteria.andSpecIdsLike("%" + typeTemplate.getSpecIds() + "%");
            }
            if (typeTemplate.getBrandIds() != null && typeTemplate.getBrandIds().length() > 0) {
                criteria.andBrandIdsLike("%" + typeTemplate.getBrandIds() + "%");
            }
            if (typeTemplate.getCustomAttributeItems() != null && typeTemplate.getCustomAttributeItems().length() > 0) {
                criteria.andCustomAttributeItemsLike("%" + typeTemplate.getCustomAttributeItems() + "%");
            }

        }

        Page<TbTypeTemplate> page = (Page<TbTypeTemplate>) typeTemplateMapper.selectByExample(example);
        //存入数据到缓存
        if (flag) {
            saveToRedis();
            flag = false;
        }
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @param id
     * @description: 根据规格id查询规格选项([ { " id " : 27, " text " : " 网络制式 ", options : [ { " xx " : " xxx ", " xxxx " : " xxxx " }, { }, { } ] }, { }, { } ])
     * @return: java.util.List<java.util.Map>
     * @author: YangRunTao
     * @date: 2019/05/18 8:44
     * @throws:
     **/
    @Override
    public List<Map> findSpecOptionsByTypeId(Long id) {
        //首先从模板表中查询出规格信息
        TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        //[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]--->list中涛map
        String specIds = tbTypeTemplate.getSpecIds();
        //将json数据转化为list
        List<Map> maps = JSON.parseArray(specIds, Map.class);
        //遍历得到的list为其中的map增加一个新的键值对options：[{},{}]，其值为一个list套map(查询就可以有框架封装得到)
        for (Map map : maps) {
            //取出规格中id的值用于查询规格选项
            Integer specIdValue = (Integer) map.get("id");
            //根据此id的值去查询规格选项
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(new Long(specIdValue));
            List<TbSpecificationOption> tbSpecificationOptions = specificationOptionMapper.selectByExample(example);
            //构建键值对(新增一个map的键值对)
            map.put("options", tbSpecificationOptions);
        }
        return maps;
    }

    /**
     * @param
     * @description: 保存模板数据(规格, 品牌)到redis
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/23 14:56
     * @throws:
     **/
    private void saveToRedis() {
        List<TbTypeTemplate> typeTemplates = findAll();

        for (TbTypeTemplate typeTemplate : typeTemplates) {
            //保存品牌({key=模板id,value=品牌数据})
            List<Map> brandList = JSON.parseArray(typeTemplate.getBrandIds(), Map.class);
            redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(), brandList);
            //存储规格列表(包含规格选项)
            List<Map> specList = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
            //循环遍历得到规格id查询规格选项
            for (Map map : specList) {
                long idLong = (Integer) map.get("id");
                //查询规格列表
                //规格选项选项条件查询
                TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
                TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
                criteria.andSpecIdEqualTo(idLong);
                List<TbSpecificationOption> tbSpecificationOptionsList = specificationOptionMapper.selectByExample(tbSpecificationOptionExample);
                map.put("options",tbSpecificationOptionsList);
            }
            redisTemplate.boundHashOps("specList").put(typeTemplate.getId(), specList);
            System.out.println("保存(规格, 品牌)到redis");
        }
    }
}
