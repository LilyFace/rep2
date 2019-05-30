package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: sku搜索类
 * @Date: 2019/05/22 16:17
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
public interface ItemSearchService {
    /**
     * @param searchMap
     * @description: sku搜索方法
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @author: YangRunTao
     * @date: 2019/05/22 16:19
     * @throws:
     **/
    Map<String, Object> search(Map searchMap);

    /**
     * @param
     * @description: 导入商品数据
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/27 14:54
     * @throws:
     **/
    void importItemListInSolr(List<TbItem> list);

    /**
     * @param ids
     * @description: 删除商品数据
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/27 15:22
     * @throws:
     **/
    void deleteItemListInSolr(Long[] ids);
}
