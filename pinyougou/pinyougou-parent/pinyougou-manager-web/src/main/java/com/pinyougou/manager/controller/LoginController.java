package com.pinyougou.manager.controller;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author: YangRunTao
 * @Description: 登录控制层(用于回显)
 * @Date: 2019/05/12 10:44
 * @Modified By:
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/name")
    public Map name() {
        //获得spring-security的Controller上下文中的用户名用于回显
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> map = new HashMap<>();
        map.put("loginName", name);
        return map;
    }

}
