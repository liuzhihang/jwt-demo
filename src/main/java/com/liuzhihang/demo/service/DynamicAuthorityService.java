package com.liuzhihang.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * 动态权限认证
 *
 * @author liuzhihang
 * @date 2019-06-25 15:51
 */
@Slf4j
@Component(value = "dynamicAuthorityService")
public class DynamicAuthorityService {


    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        try {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails && authentication instanceof UsernamePasswordAuthenticationToken) {
                // 本次请求的uri
                String uri = request.getRequestURI();

                // 获取当前用户
                UserDetails userDetails = (UserDetails) principal;

                String username = userDetails.getUsername();
                log.info("本次用户请求认证, username:{}, uri:{}", username, uri);

                // 从数据库取逻辑
                if (username.equals("liuzhihang")){
                    Set<String> set = new HashSet<>();
                    set.add("/homeInfo");
                    set.add("/getAllUser");
                    set.add("/editUserInfo");
                    if (set.contains(uri)) {
                        return true;
                    }
                }

            }
        } catch (Exception e) {
            log.error("用户请求登录, uri:{} error", request.getRequestURI(), e);
            return false;
        }
        return false;
    }


}
