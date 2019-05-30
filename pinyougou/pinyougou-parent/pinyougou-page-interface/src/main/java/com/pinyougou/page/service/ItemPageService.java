package com.pinyougou.page.service;

/**
 * @Author: YangRunTao
 * @Description: 商品详情服务接口
 * @Date: 2019/05/28 15:56
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
public interface ItemPageService {
    /**
     * @param goodsId
     * @description: 生成商品详细页
     * @return: Boolean
     * @author: YangRunTao
     * @date: 2019/05/28 15:57
     * @throws:
     **/
    Boolean genItemHtml(Long goodsId);

    /**
     * @param goodsId
     * @description: 删除商品详细页
     * @return: java.lang.Boolean
     * @author: YangRunTao
     * @date: 2019/05/29 14:47
     * @throws:
     **/
    Boolean deleteItemHtml(Long goodsId);
}
