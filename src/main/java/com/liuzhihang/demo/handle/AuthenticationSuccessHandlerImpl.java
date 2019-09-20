package com.liuzhihang.demo.handle;

import com.alibaba.fastjson.JSON;
import com.liuzhihang.demo.bean.UserDetailsImpl;
import com.liuzhihang.demo.dto.LoginRespDto;
import com.liuzhihang.demo.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户登录成功之后的返回信息
 *
 * @author liuzhihang
 * @date 2019-06-04 14:20
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtTokenUtil.generateToken(userDetails);

        // 把生成的token更新到数据库中
        // 更新DB操作 ...

        response.setContentType("application/json;charset=UTF-8");

        LoginRespDto respDto = new LoginRespDto();
        respDto.setToken(jwtToken);
        respDto.setResultCode("0000");
        respDto.setResultMsg("登录成功");
        respDto.setResultTime(LocalDateTime.now().format(FORMATTER));

        response.getWriter().write(JSON.toJSONString(respDto));

    }
}