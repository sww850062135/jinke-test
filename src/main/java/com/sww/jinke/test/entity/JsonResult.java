package com.sww.jinke.test.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @Author: Sun Weiwen
 * @Description: 请求返回数据实体
 * @Date: 10:17 2018/11/27
 */
@Setter
@Getter
public class JsonResult {
    private String status;
    private Object status1;
    private String msg;
    private Object msg1;
    private Object result;
    private Object result1;
    private Object result2;
    private Map resultMap;
}
