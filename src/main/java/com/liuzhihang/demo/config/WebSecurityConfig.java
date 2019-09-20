package com.liuzhihang.demo.config;

import com.liuzhihang.demo.filter.CustomizeAuthenticationFilter;
import com.liuzhihang.demo.filter.JwtPerTokenFilter;
import com.liuzhihang.demo.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author liuzhihang
 * @date 2019-06-03 14:25
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Resource
    private JwtPerTokenFilter jwtPerTokenFilter;

    @Resource(name = "authenticationEntryPointImpl")
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Resource(name = "authenticationSuccessHandlerImpl")
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Resource(name = "authenticationFailureHandlerImpl")
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Resource(name = "accessDeniedHandlerImpl")
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * 创建用于认证授权的用户
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureUserInfo(AuthenticationManagerBuilder auth) throws Exception {

        // 放入自己的认证授权用户, 内部逻辑需要自己实现
        // UserDetailServiceImpl implements UserDetailsService
        auth.userDetailsService(userDetailServiceImpl);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 使用JWT, 关闭session
                .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint)

                // 登录的权限, 成功返回信息, 失败返回信息
                .and().formLogin().permitAll()

                .loginProcessingUrl("/login")

                // 配置url 权限 antMatchers: 匹配url 权限
                .and().authorizeRequests()
                .antMatchers("/login", "/getVersion")
                .permitAll()
                // 其他需要登录才能访问
                .anyRequest().access("@dynamicAuthorityService.hasPermission(request,authentication)")

                // 访问无权限 location 时
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)

                // 自定义过滤
                .and().addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtPerTokenFilter, UsernamePasswordAuthenticationFilter.class)

                .headers().cacheControl();

    }


    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        /**
         * BCryptPasswordEncoder：相同的密码明文每次生成的密文都不同，安全性更高
         */
        return new BCryptPasswordEncoder();
    }

    @Bean
    CustomizeAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomizeAuthenticationFilter filter = new CustomizeAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

}
