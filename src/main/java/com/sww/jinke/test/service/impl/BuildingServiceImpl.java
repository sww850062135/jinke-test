package com.sww.jinke.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sww.jinke.test.entity.AJBBuilding;
import com.sww.jinke.test.entity.Building_correlations;
import com.sww.jinke.test.mapper.BuildingMapper;
import com.sww.jinke.test.mapper.CommunityMapper;
import com.sww.jinke.test.service.BuildingService;
import com.sww.jinke.test.util.*;
import com.sww.jinke.test.entity.JKBuildingBase;
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
 * @Date: 10:32 2018/11/12
 */
@Service
public class BuildingServiceImpl implements BuildingService{
    private static final Logger logger = LoggerFactory.getLogger(BuildingServiceImpl.class);

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private CommunityMapper communityMapper;


    /**
     * 一键拉取金科所有楼栋数据
     * @return
     */
    @Override
    public ResultMapUtil getJKBuildingList() {
        ResultMapUtil result = new ResultMapUtil();
        List<String> projectIdList = communityMapper.getJKCommunityIdList();
        System.out.println(projectIdList);
        for (int i = 0; i< projectIdList.size(); i ++) {
            try {
                HashMap<String, Object> map;
                map = SignUtils.param();
                String projectId = projectIdList.get(i);
                map.put("projectId", projectId);
                String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.getbuildlist");
                result = HttpUtils.URLPost(url, map);
                logger.info("返回的结果集: " + result);
                JSONObject jsonObject = (JSONObject) JSONObject.parse(String.valueOf(result.get("data")));
                JSONArray jsonArray = JSONObject.parseArray(String.valueOf(jsonObject.get("data")));
                System.out.println(jsonArray);
                if (jsonArray.size() == 0) {
                    logger.info("projectId:"+ projectId + " 该小区没有基础数据!");
                }else {
                    List<JKBuildingBase> jkBuildingBaseList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JKBuildingBase jkBuildingBase = JSON.parseObject(jsonArray.getString(j), JKBuildingBase.class);
                        jkBuildingBaseList.add(jkBuildingBase);
                        buildingMapper.add(jkBuildingBase);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据projectId 拉取金科楼栋数据
     * @param projectId 金科小区id
     * @return
     */
    @Override
    public ResultMapUtil getJKBuildingList(String projectId) {
        ResultMapUtil result = new ResultMapUtil();
        try {
            HashMap<String, Object> map;
            map = SignUtils.param();
            map.put("projectId", projectId);
            String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.getbuildlist");
            result = HttpUtils.URLPost(url, map);
            logger.info("返回的结果集: " + result);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(String.valueOf(result.get("data")));
            JSONArray jsonArray = JSONObject.parseArray(String.valueOf(jsonObject.get("data")));
            System.out.println(jsonArray);
            if (jsonArray.size() == 0) {
                logger.info("projectId:" + projectId + " 该小区下没有基础数据!");
            } else {
                List<JKBuildingBase> jkBuildingBaseList = new ArrayList<>();
                for (int j = 0; j < jsonArray.size(); j++) {
                    JKBuildingBase jkBuildingBase = JSON.parseObject(jsonArray.getString(j), JKBuildingBase.class);
                    jkBuildingBaseList.add(jkBuildingBase);
                    buildingMapper.add(jkBuildingBase);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将金科楼栋映射成安居宝楼栋
     * @return
     */
    @Override
    public ResultMapUtil mappedAJBBuilding() {
        ResultMapUtil result = new ResultMapUtil();
        List<AJBBuilding> ajbBuildings = new ArrayList<>();
        Building_correlations building_correlations = new Building_correlations();
        List<JKBuildingBase> jkBuildingBaseList;
        List<String> jkCommunityIdList = buildingMapper.getJKProjectId();
        for (int i= 0; i<jkCommunityIdList.size(); i++) {
            String jkCommunityId = jkCommunityIdList.get(i);
            //根据金科小区id查询金科楼栋列表
            jkBuildingBaseList = buildingMapper.getJKBuildingListByProjectId(jkCommunityId);
            for (int j=0; j<jkBuildingBaseList.size(); j++) {
                JKBuildingBase jkBuildingBase = jkBuildingBaseList.get(j);
                AJBBuilding ajbBuilding = new AJBBuilding();
                String jkBuildingId = jkBuildingBase.getBuildId();
                //查询楼栋关联表关联记录
                int count = buildingMapper.getCountByJKBuildingId(jkBuildingId);
                //如果count小于1，则准备映射楼栋数据
                if (count<1) {
                    //根据金科小区id在小区关联表中查询安居宝小区id
                    String ajbCommunityId = communityMapper.getAJBCommunityId(jkCommunityId);
                    //映射楼栋
                    ajbBuilding.setId(StringUtil.UUID());
                    ajbBuilding.setCommunityId(ajbCommunityId);
                    if (j+1<10) {
                        String ajbBuildingCode = "0"+ String.valueOf(j+1);
                        ajbBuilding.setBuildingCode(ajbBuildingCode);
                    }else {
                        String ajbBuildingCode = String.valueOf(j+1);
                        ajbBuilding.setBuildingCode(ajbBuildingCode);
                    }
                    ajbBuilding.setBuildingName(ajbBuilding.getBuildingCode() + "栋");
                    ajbBuilding.setCreatedTime(DateUtils.getCurrentDateTime());
                    //映射楼栋关联
                    building_correlations.setId(StringUtil.UUID());
                    building_correlations.setJkcommunityId(jkCommunityId);
                    building_correlations.setJkbuildingId(jkBuildingId);
                    building_correlations.setAjbbuildingId(ajbBuilding.getId());
                    ajbBuildings.add(ajbBuilding);
                    buildingMapper.addAJBBuilding(ajbBuilding);
                    buildingMapper.addBuildingCorrelations(building_correlations);
                    result.put("message", "完成映射!");
                } else {
                    result.put("message", "已存在映射关系,无须再映射!");
                }
            }
        }
        result.put("data", ajbBuildings);
        System.out.println(result.get("message"));
        return result;
    }

    /**
     * 根据金科小区id查询金科楼栋数据
     * @param projectId
     * @return
     */
    @Override
    public List<JKBuildingBase> getJKBuildingByProjectId(String projectId) {
        return buildingMapper.getJKBuildingListByProjectId(projectId);
    }

    /**
     * 根据jkProjectId和jkBuildId查询金科楼栋数据
     * @param projectId
     * @param buildId
     * @return
     */
    @Override
    public List<JKBuildingBase> getJKBuildByBuildId(String projectId, String buildId) {
        return buildingMapper.getJKBuildByBuildId(projectId, buildId);
    }

    /**
     * 根据金科小区id 查询安居宝小区(已映射的)
     * @param projectId
     * @return
     */
    @Override
    public List<AJBBuilding> getAJBBuildByProjectId(String projectId) {
        return buildingMapper.getAJBBuildByProjectId(projectId);
    }

    /**
     * 根据金科楼栋id查询映射的安居宝楼栋
     * @param jkBuildId
     * @return
     */
    @Override
    public List<AJBBuilding> getAJBBuildByJKBuildId(String jkBuildId) {
        return buildingMapper.getAJBBuildByJKBuildId(jkBuildId);
    }

}
