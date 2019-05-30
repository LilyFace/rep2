package com.pinyougou.page.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: 生成商品详细页
 * @Date: 2019/05/28 16:37
 * @Modified By:
 */
@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "JavaDoc"})
@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Value("${pagedir}")
    private String pagedir;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;


    /**
     * @param goodsId
     * @description: 生成商品详细页
     * @return: java.lang.Boolean
     * @author: YangRunTao
     * @date: 2019/05/28 20:37
     * @throws:
     **/
    @Override
    public Boolean genItemHtml(Long goodsId) {
        Writer out = null;
        try {
            //获得模板
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");

            //保存数据
            Map<String, Object> dataMap = new HashMap<>();

            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);

            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

            //查询spu信息
            if (tbGoods != null && goodsDesc != null) {
                dataMap.put("goods", tbGoods);
                dataMap.put("goodsDesc", goodsDesc);
                //查询商品种类信息
                TbItemCat tbItemCat1 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());
                TbItemCat tbItemCat2 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());
                TbItemCat tbItemCat3 = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
                dataMap.put("tbItemCat1", tbItemCat1);
                dataMap.put("tbItemCat2", tbItemCat2);
                dataMap.put("tbItemCat3", tbItemCat3);
                //查询sku信息
                TbItemExample example = new TbItemExample();
                TbItemExample.Criteria criteria = example.createCriteria();
                //当前spu的sku
                criteria.andGoodsIdEqualTo(goodsId);
                //上架状态
                criteria.andStatusEqualTo("1");
                //按照状态降序，保证第一个为默认
                example.setOrderByClause("is_default desc");
                List<TbItem> tbItems = itemMapper.selectByExample(example);
                dataMap.put("itemList", tbItems);
                System.out.println("\033[32;4m" + "写出静态文件" + "\033[0m");
            } else {
                throw new RuntimeException("出现错误,商品详情页面未生成,联系管理员！");
            }


            //写出数据
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(pagedir + goodsId + ".html")), StandardCharsets.UTF_8));
            template.process(dataMap, out);
            //关流
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param goodsId
     * @description: 删除详情页
     * @return: java.lang.Boolean
     * @author: YangRunTao
     * @date: 2019/05/29 14:37
     * @throws:
     **/
    @Override
    public Boolean deleteItemHtml(Long goodsId) {
        File file = new File(pagedir + goodsId + ".html");
        System.out.println("\033[32;4m" + "删除静态文件" + "\033[0m");
        return FileUtils.deleteQuietly(file);
    }
}
