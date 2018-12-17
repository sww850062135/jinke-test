package com.sww.jinke.test.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.sww.jinke.test.entity.AJBCommunity;
import com.sww.jinke.test.entity.Community_correlations;
import com.sww.jinke.test.mapper.CommunityMapper;
import com.sww.jinke.test.service.CommunityService;
import com.sww.jinke.test.util.*;
import com.sww.jinke.test.entity.JKCommunityBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 8:44 2018/11/13
 */
@Service
public class CommunityServiceImpl implements CommunityService {
    private static final Logger logger = LoggerFactory.getLogger(CommunityServiceImpl.class);
    @Autowired
    private CommunityMapper communityMapper;

    /**
     * 拉取金科小区数据
     * @return
     */
    @Override
    public ResultMapUtil getJKCommunityList(String companyId) {
        ResultMapUtil result = new ResultMapUtil();
        HashMap<String, Object> map;
        try {
            map = SignUtils.param();
            //可选
            map.put("companyId",companyId);
            if (StringUtil.isNullOrEmpty(companyId)){
                map.remove("companyId");
            }
            String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.getcommunitylist");
            result = HttpUtils.URLPost(url, map);
            logger.info("返回的结果集：" + result);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(String.valueOf(result.get("data")));
            JSONArray jsonArray = JSONObject.parseArray(String.valueOf(jsonObject.get("data")));
            List<JKCommunityBase> jkCommunityBaseList = new ArrayList<>();
            for (int i=0; i<jsonArray.size(); i++){
                JKCommunityBase jkCommunityBase = JSONObject.parseObject(jsonArray.getString(i), JKCommunityBase.class);
                jkCommunityBaseList.add(jkCommunityBase);
                communityMapper.add(jkCommunityBase);
            }
            System.out.println(jkCommunityBaseList.toString());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 分页查询金科小区数据
     * @param pageNum 当前页
     * @param pageSize 每页显示的页面数
     * @return
     * @throws Exception
     */
    @Override
    public List<JKCommunityBase> selectJKCommunityList(int pageNum, int pageSize) throws Exception {
        //分页
        PageHelper.startPage(pageNum, pageSize);
        return communityMapper.getJKcommunitylist();
    }

    /**
     * 根据金科小区Id(项目Id)查询金科小区数据
     * @param projectId
     * @return
     */
    @Override
    public List<JKCommunityBase> selectJKCommunityByProjectId(String projectId) {
        return communityMapper.getJKcommunityByProjectId(projectId);
    }

    /**
     * 将金科小区映射成安居宝小区
     * @return
     */
    @Override
    public ResultMapUtil mappedAJBCommunityList() {
        ResultMapUtil result = new ResultMapUtil();
        List<JKCommunityBase> jkCommunityBaseList;
        List<AJBCommunity> communities = new ArrayList<>();
        Community_correlations community_correlations = new Community_correlations();
        jkCommunityBaseList = communityMapper.getJKcommunitylist();
        System.out.println(jkCommunityBaseList);
        for (int i = 0; i< jkCommunityBaseList.size(); i++){
            AJBCommunity ajbCommunity = new AJBCommunity();
            JKCommunityBase jkCommunityBase = jkCommunityBaseList.get(i);
            String EsProjectId = jkCommunityBase.getProjectId();
            int count = communityMapper.getCountByjk_community_id(EsProjectId);
            //如果count小于1,说明该金科小区数据还没有映射
            if (count<1) {
                //映射安居宝小区
                ajbCommunity.setId(StringUtil.UUID());
                ajbCommunity.setCommunityCode(StringUtil.RandNumberSex());
                ajbCommunity.setCommunityName(jkCommunityBase.getProjectName());
                ajbCommunity.setProvinceName(jkCommunityBase.getProvinceName());
                ajbCommunity.setCityName(jkCommunityBase.getCityName());
                ajbCommunity.setDistrictName(jkCommunityBase.getDistrictName());
                ajbCommunity.setCreatedTime(DateUtils.getCurrentDateTime());

                //映射小区关联
                community_correlations.setId(StringUtil.UUID());
                community_correlations.setAjbcommunityId(ajbCommunity.getId());
                community_correlations.setJkcommunityId(jkCommunityBase.getProjectId());
                community_correlations.setAjbcommunityCode(ajbCommunity.getCommunityCode());

                communities.add(ajbCommunity);
                result.put("message","映射完成!");
                communityMapper.addAJBCommunity(ajbCommunity);
                communityMapper.addCommunity_correlations(community_correlations);
            }else {
                result.put("message", "已存在映射关系，无须再映射");
            }

        }
        result.put("data", communities);
        System.out.println(result.get("message"));
        return result;
    }

    /**
     * 查询金科小区列表
     * @return
     */
    @Override
    public List<JKCommunityBase> selectJKCommunityList() {
        return communityMapper.getJKcommunitylist();
    }

    /**
     * 根据金科项目ID查询安居宝小区
     * @param projectId
     * @return
     */
    @Override
    public List<AJBCommunity> selectAJBCommunityByProjectId(String projectId) {
        return communityMapper.getAJBCommunityByProjectId(projectId);
    }

    @Override
    public List<AJBCommunity> selectAJBCommunityList(int pageNum, int pageSize) throws Exception {
        //分页
        PageHelper.startPage(pageNum, pageSize);
        return communityMapper.getAJBCommunityList();
    }

    /**
     * 根据金科项目ID查询安居宝小区编码
     * @param projectId
     * @return
     */
    @Override
    public String selectAJBCommunityCode(String projectId) {
        return communityMapper.getAJBCommunityCode(projectId);
    }


}
