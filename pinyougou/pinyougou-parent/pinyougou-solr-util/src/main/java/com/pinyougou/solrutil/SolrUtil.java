package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: 商品数据的查询(已审核商品)
 * @Date: 2019/05/22 14:52
 * @Modified By:
 */
@SuppressWarnings({"JavaDoc"})
@Component
public class SolrUtil {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;


    /**
     * @param
     * @description: 导入商品数据
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/22 14:55
     * @throws:
     **/
    public void importItemData() {
        //1.查询可以导入的商品(已审核商品)
        TbItemExample tbItemExample = new TbItemExample();
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> tbItems = itemMapper.selectByExample(tbItemExample);
        System.out.println("===商品列表===");
        for (TbItem item : tbItems) {
            Map specMap= JSON.parseObject(item.getSpec());//将spec字段中的json字符串转换为map
            //noinspection unchecked
            item.setSpecMap(specMap);//给带注解的字段赋值
            System.out.println(item.getTitle());
        }
        System.out.println("===结束===");

        //2.将数据保存到solr
        solrTemplate.saveBeans(tbItems);
        solrTemplate.commit();
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();
    }
}
