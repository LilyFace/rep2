package com.pinyougou.search.service.impl;

import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @Author: YangRunTao
 * @Description: 消费者删除solr的监听器
 * @Date: 2019/05/30 16:49
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
public class ItemDeleteListener implements MessageListener {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ItemSearchService itemSearchService;

    /**
     * @param message
     * @description: 消费者删除solr
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/30 18:59
     * @throws:
     **/
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("ItemDeleteListener监听接收到消息..." + Arrays.toString(ids));
            itemSearchService.deleteItemListInSolr(ids);
            System.out.println("成功删除索引库中的记录");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
