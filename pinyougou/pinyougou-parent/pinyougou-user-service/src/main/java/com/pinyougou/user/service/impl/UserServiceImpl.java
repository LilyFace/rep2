package com.pinyougou.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.pojo.TbUserExample;
import com.pinyougou.pojo.TbUserExample.Criteria;

import entity.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@SuppressWarnings("JavaDoc")
@Service
public class UserServiceImpl implements UserService {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TbUserMapper userMapper;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(null);
        //noinspection unchecked
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbUser user) {
        if (user != null && StringUtils.isNotEmpty(user.getUsername()) && StringUtils.isNotEmpty(user.getPassword())) {
            user.setCreated(new Date());//创建日期
            user.setUpdated(new Date());//修改日期
            String password = DigestUtils.md5Hex(user.getPassword());//对密码加密
            user.setPassword(password);
            userMapper.insert(user);
        }
    }


    /**
     * 修改
     */
    @Override
    public void update(TbUser user) {
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbUser findOne(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            userMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbUser user, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();

        if (user != null) {
            if (user.getUsername() != null && user.getUsername().length() > 0) {
                criteria.andUsernameLike("%" + user.getUsername() + "%");
            }
            if (user.getPassword() != null && user.getPassword().length() > 0) {
                criteria.andPasswordLike("%" + user.getPassword() + "%");
            }
            if (user.getPhone() != null && user.getPhone().length() > 0) {
                criteria.andPhoneLike("%" + user.getPhone() + "%");
            }
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                criteria.andEmailLike("%" + user.getEmail() + "%");
            }
            if (user.getSourceType() != null && user.getSourceType().length() > 0) {
                criteria.andSourceTypeLike("%" + user.getSourceType() + "%");
            }
            if (user.getNickName() != null && user.getNickName().length() > 0) {
                criteria.andNickNameLike("%" + user.getNickName() + "%");
            }
            if (user.getName() != null && user.getName().length() > 0) {
                criteria.andNameLike("%" + user.getName() + "%");
            }
            if (user.getStatus() != null && user.getStatus().length() > 0) {
                criteria.andStatusLike("%" + user.getStatus() + "%");
            }
            if (user.getHeadPic() != null && user.getHeadPic().length() > 0) {
                criteria.andHeadPicLike("%" + user.getHeadPic() + "%");
            }
            if (user.getQq() != null && user.getQq().length() > 0) {
                criteria.andQqLike("%" + user.getQq() + "%");
            }
            if (user.getIsMobileCheck() != null && user.getIsMobileCheck().length() > 0) {
                criteria.andIsMobileCheckLike("%" + user.getIsMobileCheck() + "%");
            }
            if (user.getIsEmailCheck() != null && user.getIsEmailCheck().length() > 0) {
                criteria.andIsEmailCheckLike("%" + user.getIsEmailCheck() + "%");
            }
            if (user.getSex() != null && user.getSex().length() > 0) {
                criteria.andSexLike("%" + user.getSex() + "%");
            }

        }

        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(example);
        //noinspection unchecked
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination smsDestination;

    @Value("${template_code}")
    private String template_code;
    @Value("${sign_name}")
    private String sign_name;
    @Value("${accessKeyId}")
    private String accessKeyId;
    @Value("${accessKeySecret}")
    private String accessKeySecret;

    /**
     * @param phoneNum
     * @description: 生成短信验证码
     * map.put("accessKeyId", env.getProperty("accessKeyId"));
     * map.put("accessSecret", env.getProperty("accessKeySecret"));
     * map.put("action", "SendSms");
     * map.put("phoneNumbers", "18428384101");
     * map.put("signName", "pinyougou");
     * map.put("templateCode", "SMS_166865423");
     * map.put("templateParamJson", "{\"code\":\"589562\"}");
     * @return: void
     * @author: YangRunTao
     * @date: 2019/06/02 11:48
     * @throws:
     **/
    @Override
    public void createSmsCheckcode(String phoneNum) {
        if (StringUtils.isNotEmpty(phoneNum)) {
            //生成6位随机数
            Long codeLong = (long) (Math.random() * 1000000);
            String code = codeLong.toString();
            System.out.println("验证码：" + code);
            //存入缓存
            redisTemplate.boundHashOps("smscode").put(phoneNum, code);
            System.out.println("验证码存入缓存成功!");
            //发送到activeMQ	....

            //noinspection Convert2Lambda
            jmsTemplate.send(smsDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage message = session.createMapMessage();
                    message.setString("accessKeyId", accessKeyId);
                    message.setString("accessSecret", accessKeySecret);
                    message.setString("action", "SendSms");
                    message.setString("phoneNumbers", phoneNum);
                    message.setString("signName", sign_name);
                    message.setString("templateCode", template_code);
                    Map<String, String> map = new HashMap<>();
                    map.put("code", code);
                    System.out.println(map);
                    message.setString("templateParamJson", JSON.toJSONString(map));
                    return message;
                }
            });

        } else {
            throw new RuntimeException("电话号码为空");
        }
    }

    /**
     * @param phone
     * @param code
     * @description: 检验验证码
     * @return: boolean
     * @author: YangRunTao
     * @date: 2019/06/02 14:39
     * @throws:
     **/
    @Override
    public boolean checkSmsCode(String phone, String code) {
        String sysCode = (String) redisTemplate.boundHashOps("smscode").get(phone);
        if (sysCode == null) {
            return false;
        }
        if (!sysCode.equals(code)) {
            return false;
        } else {
            redisTemplate.boundHashOps("smscode").delete(phone);
            System.out.println("验证码比对成功,删除验证码");
            return true;
        }
    }
}
