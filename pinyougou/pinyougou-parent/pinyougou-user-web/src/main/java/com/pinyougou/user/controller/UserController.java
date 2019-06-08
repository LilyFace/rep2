package com.pinyougou.user.controller;

import java.util.List;

import com.pinyougou.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbUser;


import entity.PageResult;
import entity.Result;
import util.PhoneFormatCheckUtils;

/**
 * controller
 *
 * @author Administrator
 */
@SuppressWarnings("JavaDoc")
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbUser> findAll() {
        return userService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return userService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param user
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbUser user,String smscode) {
        if (StringUtils.isEmpty(smscode)){
            return new Result(false,"验证码不能为空");
        }else {
            if (userService.checkSmsCode(user.getPhone(),smscode)){
                try {
                    userService.add(user);
                    return new Result(true, "增加成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result(false, "增加失败");
                }
            }else{
                return new Result(false,"验证码不正确");
            }
        }
    }

    /**
     * 修改
     *
     * @param user
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbUser user) {
        try {
            userService.update(user);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbUser findOne(Long id) {
        return userService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            userService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param user
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbUser user, int page, int rows) {
        return userService.findPage(user, page, rows);
    }

    /**
     * @param phoneNum
     * @description: 发送短信验证码
     * @return: entity.Result
     * @author: YangRunTao
     * @date: 2019/06/02 14:05
     * @throws:
     **/
    @RequestMapping("/sendCode")
    public Result sendCode(String phoneNum) {
        if (!PhoneFormatCheckUtils.isPhoneLegal(phoneNum)) {
            return new Result(false, "手机号格式不正确");
        } else {
            try {
                userService.createSmsCheckcode(phoneNum);//生成验证码
                return new Result(true, "验证码发送成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(true, "验证码发送失败");
            }

        }
    }


}
