package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: sku搜索类
 * @Date: 2019/05/22 16:35
 * @Modified By:
 */
@SuppressWarnings({"JavaDoc", "SpringJavaAutowiredMembersInspection"})
@Service
public class ItemSearchServiceImpl implements ItemSearchService {
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * @param searchMap
     * @description: sku搜索方法
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @author: YangRunTao
     * @date: 2019/05/22 16:36
     * @throws:
     **/
    @Override
    public Map<String, Object> search(Map searchMap) {
        Map<String, Object> map = new HashMap<>();

        //条件查询对象
       /* Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        query.addCriteria(criteria);
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        map.put("rows", page.getContent());*/

        //商品列表搜索
        //noinspection CollectionAddAllCanBeReplacedWithConstructor
        map.putAll(searchItemList(searchMap));

        //商品分类搜索
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList", categoryList);

        //商品品牌和规格
       /* if (categoryList != null && categoryList.size() > 0) {
            map.putAll(searchBrandAndSpecList(categoryList.get(0)));
        }*/
        String category = (String) searchMap.get("category");
        if (!category.equals("")) {
            map.putAll(searchBrandAndSpecList(category));
        } else {
            if (categoryList.size() > 0) {
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }

        return map;
    }

    /**
     * @param searchMap
     * @description: 商品列表搜索sku
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @author: YangRunTao
     * @date: 2019/05/23 9:16
     * @throws:
     **/
    private Map<String, Object> searchItemList(Map searchMap) {
        Map<String, Object> map = new HashMap<>();
        //关键字空格处理
        String keywordsHasSpace = (String) searchMap.get("keywords");
        String keywords = keywordsHasSpace.replace(" ", "");


        //高亮查询
        HighlightQuery query = new SimpleHighlightQuery();
        //设置高亮的域
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        //设置高亮前缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        //设置高亮后缀
        highlightOptions.setSimplePostfix("</em>");
        //给查询对象注入高亮选项
        query.setHighlightOptions(highlightOptions);

        //按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(keywords);

        /*
            设置条件查询的条件
        */
        //1.种类条件
        if (StringUtils.isNotEmpty((String) searchMap.get("category"))) {
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //2.品牌条件
        if (StringUtils.isNotEmpty((String) searchMap.get("brand"))) {
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //3.规格条件
        if (searchMap.get("spec") != null) {
            @SuppressWarnings("unchecked")
            Map<String, String> specMap = (Map) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
                FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //4.价格条件
        if (StringUtils.isNotEmpty((String) searchMap.get("price"))) {
            //切割价格
            String[] prices = ((String) searchMap.get("price")).split("-");

            //如果区间不包含0则加入价格起始条件
            if (!prices[0].equals("0")) {
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            //如果区间不包含*则加入价格结束条件
            if (!prices[1].equals("*")) {
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //5.分页条件
        //提取页码
        Integer pageNo = (Integer) searchMap.get("pageNo");
        if (StringUtils.isEmpty(pageNo.toString())) {
            //若没有页码默认为第一页
            pageNo = 1;
        }/* else {
            //有页码,转化页码赋值给pageNo
            pageNo = Integer.parseInt(pageNoString);
        }*/
        //提取每页显示条数
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (StringUtils.isEmpty(pageSize.toString())) {
            //若没有每页显示条数显示20条
            pageSize = 1;
        } /*else {
            //有每页显示条数,转化每页显示条数赋值给pageSize
            pageSize = Integer.parseInt(pageSizeString);
        }*/
        query.setOffset((pageNo - 1) * pageSize);//从第几条记录查询10 10
        query.setRows(pageSize);


        //注入查询对象
        query.addCriteria(criteria);
        //获得结果
        HighlightPage<TbItem> tbItems = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //循环高亮入口集合
        for (HighlightEntry<TbItem> tbItemHighlightEntry : tbItems.getHighlighted()) {
            //获得源实体
            TbItem tbItem = tbItemHighlightEntry.getEntity();
            //判断存在高亮域集合并且集合中的高亮域中保存的值至少有一个以上()
            if (tbItemHighlightEntry.getHighlights().size() > 0
                    && tbItemHighlightEntry.getHighlights().get(0).getSnipplets().size() > 0) {//每个域有可能存储多值
                //存在将高亮的结果赋值给返回的实体
                tbItem.setTitle(tbItemHighlightEntry.getHighlights().get(0).getSnipplets().get(0));//设置高亮结果
            }
        }
        map.put("rows", tbItems.getContent());
        map.put("totalPages", tbItems.getTotalPages());//返回总页数
        map.put("total", tbItems.getTotalElements());//返回总记录数
        return map;
    }

    /**
     * @param searchMap
     * @description: 查询分类列表
     * @return: java.util.List
     * @author: YangRunTao
     * @date: 2019/05/23 9:16
     * @throws:
     **/
    private List<String> searchCategoryList(Map searchMap) {
        List<String> list = new ArrayList<>();
        Object keywords = searchMap.get("keywords");
        //使用solr的分组查询
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        query.addCriteria(criteria);

        //根据分类分组(根据商品类型分组)
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        //获得分组页
        GroupPage<TbItem> tbItems = solrTemplate.queryForGroupPage(query, TbItem.class);
        //得到分组结果入口页(商品类型结果页)
        GroupResult<TbItem> item_keywordGroupResult = tbItems.getGroupResult("item_category");
        //得到分组结果入口集合
        Page<GroupEntry<TbItem>> groupEntries = item_keywordGroupResult.getGroupEntries();
        if (groupEntries != null && groupEntries.getTotalElements() > 0) {
            //循环入口集合
            for (GroupEntry<TbItem> groupEntry : groupEntries) {
                //获得分组中的每个元素的值
                String groupValue = groupEntry.getGroupValue();
                //将分组结果的名称封装到返回值中
                list.add(groupValue);
            }
        }
        return list;
    }

    /**
     * @param category
     * @description: 查询品牌和规格列表
     * @return: java.util.Map
     * @author: YangRunTao
     * @date: 2019/05/23 15:53
     * @throws:
     **/
    @SuppressWarnings("unchecked")
    private Map<String, Object> searchBrandAndSpecList(String category) {
        Map<String, Object> map = new HashMap<>();
        /*
            从缓存中获得品牌和规格列表
        */
        //1.根据传入的种类查询模板id
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        //2.根据模板id查询品牌和规格列表
        if (typeId != null) {
            //根据模板ID查询品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList", brandList);//返回值添加品牌列表
            //根据模板ID查询规格列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList", specList);
        }
        return map;
    }

}
