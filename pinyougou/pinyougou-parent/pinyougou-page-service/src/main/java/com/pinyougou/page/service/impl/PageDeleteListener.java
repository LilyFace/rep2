package com.pinyougou.page.service.impl;

import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @Author: YangRunTao
 * @Description: 静态页面发布/订阅模式的消费者监听器(删除静态页面)
 * @Date: 2019/05/30 21:13
 * @Modified By:
 */
@SuppressWarnings("JavaDoc")
public class PageDeleteListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    /**
     * @param message
     * @description: (删除静态页面)
     * @return: void
     * @author: YangRunTao
     * @date: 2019/05/30 21:14
     * @throws:
     **/
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("ItemDeleteListener监听接收到消息..." + goodsIds);
            boolean b = itemPageService.deleteItemHtml(goodsIds);
            System.out.println("网页删除结果：" + b);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
