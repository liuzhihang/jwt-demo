package com.liuzhihang.demo.handle;

import com.alibaba.fastjson.JSON;
import com.liuzhihang.demo.dto.LoginRespDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 当用户访问无权限页面时, 返回信息
 *
 * @author liuzhihang
 * @date 2019-06-04 14:03
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        LoginRespDto respDto = new LoginRespDto();
        respDto.setResultCode("0002");
        respDto.setResultMsg("用户无权访问");
        respDto.setResultTime(LocalDateTime.now().format(FORMATTER));

        response.getWriter().write(JSON.toJSONString(respDto));

    }
}
