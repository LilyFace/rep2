package com.pinyougou.user.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: YangRunTao
 * @Description: 登录控制层(展示登录名)
 * @Date: 2019/06/03 21:32
 * @Modified By:
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/name")
    public Map<String, String> showName() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();//得到登陆人账号
        Map<String, String> map = new HashMap<>();
        map.put("loginName", name);
        return map;
    }
}
