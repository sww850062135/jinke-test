package com.sww.jinke.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.sww.jinke.test.mapper.CompanyMapper;
import com.sww.jinke.test.service.CompanyService;
import com.sww.jinke.test.util.HttpUtils;
import com.sww.jinke.test.util.PropertyUtil;
import com.sww.jinke.test.util.ResultMapUtil;
import com.sww.jinke.test.util.SignUtils;

import com.sww.jinke.test.entity.JKCompanyBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @Author: Sun Weiwen
 * @Description: 
 * @Date: 14:34 2018/11/13
 */
@Service
public class CompanyServiceImpl implements CompanyService{

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 获取金科分公司列表
     * @return
     */
    @Override
    public ResultMapUtil getCompanyList() {
        ResultMapUtil result = new ResultMapUtil();
        HashMap<String, Object> map;
        try {
            map = SignUtils.param();
            String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.getcompanylist");
            result = HttpUtils.URLPost(url, map);
            System.out.println("返回的结果集：" + result);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(String.valueOf(result.get("data")));
            JSONArray jsonArray = JSONObject.parseArray(String.valueOf(jsonObject.get("data")));
            List<JKCompanyBase> companyList = new ArrayList<>();
            for (int i = 0;i<jsonArray.size();i++){
                JKCompanyBase jkCompanyBase = JSON.parseObject(jsonArray.getString(i), JKCompanyBase.class);
                companyList.add(jkCompanyBase);
                companyMapper.add(jkCompanyBase);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
