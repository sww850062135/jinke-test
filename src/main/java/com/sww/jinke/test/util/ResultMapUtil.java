package com.sww.jinke.test.util;

import java.util.HashMap;
/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 9:41 2018/11/15
 */
public class ResultMapUtil extends HashMap<String, Object>{
    public ResultMapUtil(){}

    public ResultMapUtil success() {
        this.put("code", 200);
        return this;
    }

    public ResultMapUtil fail(int code) {
        this.put("code", code);
        return this;
    }

    public ResultMapUtil message(String message){
        this.put("message", message);
        return this;
    }

    public ResultMapUtil data(Object object){
        this.put("data", object);
        return this;
    }

}
