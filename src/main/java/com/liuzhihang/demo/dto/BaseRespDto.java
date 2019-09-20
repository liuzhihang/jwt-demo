package com.liuzhihang.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuzhihang
 * @date 2019-06-12 16:49
 */
@Data
public class BaseRespDto implements Serializable {

    private String resultCode;

    private String resultMsg;

    private String resultTime;

}
