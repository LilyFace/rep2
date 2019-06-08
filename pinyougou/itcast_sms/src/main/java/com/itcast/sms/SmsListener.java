package com.itcast.sms;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: 消息监听类
 * @Date: 2019/06/01 18:02
 * @Modified By:
 */
@Component
public class SmsListener {

    private SmsUtil smsUtil;

    @Autowired
    public void setSmsUtil(SmsUtil smsUtil) {
        this.smsUtil = smsUtil;
    }

    @JmsListener(destination = "sms")
    public void sendSms(Map<String, String> map) {
        try {
            smsUtil.message(map.get("accessKeyId"),
                    map.get("accessSecret"),
                    map.get("action"),
                    map.get("phoneNumbers"),
                    map.get("signName"),
                    map.get("templateCode"),
                    map.get("templateParamJson"));
            System.out.println("发送短信成功");
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("发送短信失败");
        }
    }
}
