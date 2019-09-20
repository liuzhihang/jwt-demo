package com.liuzhihang.demo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 
 * 自定义拦截器, 重写UsernamePasswordAuthenticationFilter 从而可以处理 application/json 中的json请求报文
 * 
 * @author liuzhihang
 * @date 2019-06-12 19:04
 */
@Slf4j
public class CustomizeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        // attempt Authentication when Content-Type is json
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)
            || request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                BufferedReader br = request.getReader();
                String str;
                StringBuilder jsonStr = new StringBuilder();
                while ((str = br.readLine()) != null) {
                    jsonStr.append(str);
                }

                log.info("本次登录请求参数:{}", jsonStr);

                JSONObject jsonObject = JSON.parseObject(jsonStr.toString());

                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    jsonObject.getString("username"), jsonObject.getString("password"));
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                log.info("用户登录, 请求参数 不正确");
                throw new AuthenticationServiceException("获取报文请求参数失败");
            } catch (JSONException e) {
                log.info("用户登录, 请求报文格式 不正确");
                throw new AuthenticationServiceException("请求报文, 转换Json失败");
            }
        } else {
            log.error("用户登录, contentType 不正确");
            throw new AuthenticationServiceException(
                "请求 contentType 不正确, 请使用 application/json;charset=UTF-8 或者 application/json;");
        }

    }

}
