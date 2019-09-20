package com.liuzhihang.demo.service;

import com.liuzhihang.demo.bean.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author liuzhihang
 */
@Slf4j
@Component("userDetailServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {


    /**
     * 用来验证登录名是否有权限进行登录
     *
     * 可以通过数据库进行校验 也可以通过redis 等等
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        UserDetailsImpl userDetailsImpl = new UserDetailsImpl();
        userDetailsImpl.setUsername("liuzhihang");
        userDetailsImpl.setPassword(new BCryptPasswordEncoder().encode("123456789"));
        return userDetailsImpl;
    }

}
