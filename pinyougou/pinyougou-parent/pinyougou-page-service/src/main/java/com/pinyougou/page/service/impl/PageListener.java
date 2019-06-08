package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @Author: YangRunTao
 * @Description: 静态页面发布/订阅模式的消费者监听器(生成静态页面)
 * @Date: 2019/05/30 20:20
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
public class PageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;

    /**
     * @param message
     * @description: 根据消息生成静态页面
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/30 20:55
     * @throws:
     **/
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            Long goodsId = (Long) objectMessage.getObject();
            System.out.println("接收到消息：" + goodsId);
            itemPageService.genItemHtml(goodsId);
            System.out.println("页面生成通过~~~~");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
