package com.liuzhihang.demo.handle;

import com.alibaba.fastjson.JSON;
import com.liuzhihang.demo.dto.LoginRespDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用户登录认证失败返回的信息
 *
 * @author liuzhihang
 * @date 2019-06-04 13:57
 */
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        LoginRespDto respDto = new LoginRespDto();
        respDto.setResultCode("0001");
        respDto.setResultMsg("用户登录认证失败");
        respDto.setResultTime(LocalDateTime.now().format(FORMATTER));

        response.getWriter().write(JSON.toJSONString(respDto));
    }
}
