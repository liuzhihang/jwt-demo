package com.liuzhihang.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuzhihang
 * @date 2019/9/20 5:27 下午
 */
@Slf4j
@RestController
public class DemoTestController {



    @PostMapping(value = "/getVersion")
    public String getVersion() {

        return "V1.0.0";
    }

    /**
     * 对象 使用 @RequestBody 接收
     * 比如:
     *
     * public RespDto getResp(@RequestBody ReqDto reqDto) {
     *      return null;
     * }
     *
     * @return
     */
    @PostMapping(value = "/homeInfo")
    public String homeInfo() {

        return "homeInfo 收到请求";
    }

    @PostMapping(value = "/modifyName")
    public String modifyName() {

        return "modifyName 收到请求";
    }


}
