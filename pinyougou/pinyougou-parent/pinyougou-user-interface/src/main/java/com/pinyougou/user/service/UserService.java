package com.pinyougou.user.service;

import java.util.List;

import com.pinyougou.pojo.TbUser;

import entity.PageResult;

/**
 * 服务层接口
 *
 * @author Administrator
 */
@SuppressWarnings("JavaDoc")
public interface UserService {

    /**
     * 返回全部列表
     *
     * @return
     */
    List<TbUser> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    void add(TbUser user);


    /**
     * 修改
     */
    void update(TbUser user);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    TbUser findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页码
     * @param pageSize 每页记录数
     * @return
     */
    PageResult findPage(TbUser user, int pageNum, int pageSize);

    /**
     * @param
     * @description: 生成短信验证码
     * @return: void
     * @author: YangRunTao
     * @date: 2019/06/02 11:46
     * @throws:
     **/
    void createSmsCheckcode(String phoneNum);

    /**
     * @param phone
     * @param code
     * @description: 检验验证码
     * @return: boolean
     * @author: YangRunTao
     * @date: 2019/06/02 14:44
     * @throws:
     **/
    boolean checkSmsCode(String phone, String code);
}
