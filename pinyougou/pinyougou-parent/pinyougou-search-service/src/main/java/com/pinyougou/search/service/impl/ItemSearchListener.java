package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: 消费者导入solr的监听器
 * @Date: 2019/05/30 15:36
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
public class ItemSearchListener implements MessageListener {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ItemSearchService itemSearchService;

    /**
     * @param message
     * @description: 消费者导入solr
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/30 18:59
     * @throws:
     **/
    @Override
    public void onMessage(Message message) {

        try {
            System.out.println("监听接收到消息...");
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            List<TbItem> tbItems = JSON.parseArray(text, TbItem.class);
            for (TbItem tbItem : tbItems) {
                System.out.println(tbItem.getId() + " " + tbItem.getTitle());
                //将spec字段中的json字符串转换为map
                Map specMap = JSON.parseObject(tbItem.getSpec());
                //给带注解的字段赋值
                //noinspection unchecked
                tbItem.setSpecMap(specMap);
            }
            itemSearchService.importItemListInSolr(tbItems);//导入
            System.out.println("成功导入到索引库");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
