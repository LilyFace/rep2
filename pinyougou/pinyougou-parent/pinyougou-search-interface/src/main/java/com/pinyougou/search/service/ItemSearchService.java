package com.pinyougou.search.service;

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
}
