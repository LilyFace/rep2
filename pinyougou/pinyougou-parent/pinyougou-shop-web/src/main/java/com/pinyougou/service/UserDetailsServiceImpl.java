package com.pinyougou.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: YangRunTao
 * @Description: 商家服务层
 * @Date: 2019/05/13 9:23
 * @Modified By:
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    //商家商品服务
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //System.out.println("UserDetailsServiceImpl执行了");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //实际为角色表查到的角色
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        //此处的根据id正好是页面提交的username
        TbSeller tbSeller = sellerService.findOne(username);
        if (tbSeller != null) {
            if (tbSeller.getStatus().equals("1")) {
                return new User(username, tbSeller.getPassword(), grantedAuthorities);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
