package com.sww.jinke.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sww.jinke.test.entity.AJBUnit;
import com.sww.jinke.test.entity.JKBuildingBase;
import com.sww.jinke.test.entity.Unit_correlations;
import com.sww.jinke.test.mapper.BuildingMapper;
import com.sww.jinke.test.mapper.CommunityMapper;
import com.sww.jinke.test.mapper.UnitMapper;
import com.sww.jinke.test.service.UnitService;
import com.sww.jinke.test.util.*;
import com.sww.jinke.test.entity.JKUnitBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService{
    private static final Logger logger = LoggerFactory.getLogger(UnitServiceImpl.class);

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private UnitMapper unitMapper;

    /**
     * 一键拉取金科所有单元数据
     * @return
     */
    @Override
    public ResultMapUtil getJKUnitList() {
        ResultMapUtil result = new ResultMapUtil();
        List<String> projectIdList = communityMapper.getJKCommunityIdList();
        for (int i = 0; i < projectIdList.size(); i++){
            try {
                HashMap<String, Object> map;
                map= SignUtils.param();
                String projectId = projectIdList.get(i);
                map.put("projectId", projectId);
                String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.getunitlist");
                result = HttpUtils.URLPost(url, map);
                logger.info("返回的结果集: " + result);
                JSONObject jsonObject = (JSONObject) JSONObject.parse(String.valueOf(result.get("data")));
                JSONArray jsonArray = JSONObject.parseArray(String.valueOf(jsonObject.get("data")));
                if (jsonArray.size() == 0) {
                    logger.info("projectId: " + projectId + " 该项目下无基础数据!");
                }else {
                    List<JKUnitBase> jkUnitBaseList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JKUnitBase jkUnitBase = JSON.parseObject(jsonArray.getString(j), JKUnitBase.class);
                        jkUnitBaseList.add(jkUnitBase);
                        unitMapper.add(jkUnitBase);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据projectId 和 buildId拉取金科单元数据
     * @param projectId
     * @param buildId
     * @return
     */
    @Override
    public ResultMapUtil getJKUnitList(String projectId, String buildId) {
        ResultMapUtil result = new ResultMapUtil();
        try {
            HashMap<String, Object> map;
            map = SignUtils.param();
            map.put("projectId", projectId);
            map.put("buildId", buildId);
            if (StringUtil.isNullOrEmpty(buildId)) {
                map.remove("buildId");
            }
            String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.getunitlist");
            result = HttpUtils.URLPost(url, map);
            logger.info("返回的结果集: " + result);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(String.valueOf(result.get("data")));
            JSONArray jsonArray = JSONObject.parseArray(String.valueOf(jsonObject.get("data")));
            if (jsonArray.size() == 0) {
                logger.info("projectId: " + projectId + "buildId: " + buildId + "该小区楼栋下无基础数据!");
            } else {
                List<JKUnitBase> jkUnitBaseList = new ArrayList<>();
                for (int j = 0; j < jsonArray.size(); j++) {
                    JKUnitBase jkUnitBase = JSON.parseObject(jsonArray.getString(j), JKUnitBase.class);
                    jkUnitBaseList.add(jkUnitBase);
                    unitMapper.add(jkUnitBase);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将金科单元数据映射成安居宝单元数据
     * @return
     */
    @Override
    public ResultMapUtil mappedAJBUnit() {
        ResultMapUtil result = new ResultMapUtil();
        List<AJBUnit> ajbUnits = new ArrayList<>();
        Unit_correlations unit_correlations = new Unit_correlations();
        List<JKUnitBase> jkUnitBaseList;
        List<String> jkBuildingIdList = unitMapper.getJKBuildingIdList();
        for (int i = 0; i < jkBuildingIdList.size(); i++) {
            String jkBuildingId = jkBuildingIdList.get(i);
            //根据金科楼栋id查询金科单元信息
            jkUnitBaseList = unitMapper.getJKUnitListByJKBuildingId(jkBuildingId);
            for (int j = 0; j < jkUnitBaseList.size(); j++) {
                JKUnitBase jkUnitBase = jkUnitBaseList.get(j);
                AJBUnit ajbUnit = new AJBUnit();
                String jkUnitId = jkUnitBase.getUnitId();
                String jkCommunityId = jkUnitBase.getProjectId();
                String ajbBuildingId = buildingMapper.getAJBBuildingId(jkBuildingId);
                String ajbCommunityId = communityMapper.getAJBCommunityId(jkCommunityId);
                String ajbBuildingCode = buildingMapper.getAJBBuildingCode(ajbBuildingId);
                //根据jkUnitId查询单元关联表关联记录
                int count = unitMapper.getCountByJKUnitId(jkUnitId);
                //若count小于1,则该单元无关联记录,可执行映射操作
                if (count < 1) {
                    //映射单元
                    ajbUnit.setId(StringUtil.UUID());
                    ajbUnit.setCommunityId(ajbCommunityId);
                    ajbUnit.setBuildingId(ajbBuildingId);
                    String ajbUnitCode;
                    String unitNo;
                    //4位unit_code 由building_code和单元号unitNo拼接而成
                    if (ajbBuildingCode.length()<3 && j+1<10) {
                        unitNo = "0" + String.valueOf(j+1);
                        ajbUnitCode = ajbBuildingCode + unitNo;
                    } else {
                        unitNo = String.valueOf(j+1);
                        ajbUnitCode = ajbBuildingCode + unitNo;
                    }
                    ajbUnit.setUnitCode(ajbUnitCode);
                    ajbUnit.setUnitName(ajbBuildingCode + "楼" + unitNo + "单元");
                    ajbUnit.setCreatedTime(DateUtils.getCurrentDateTime());
                    //映射单元关联
                    unit_correlations.setId(StringUtil.UUID());
                    unit_correlations.setJkcommunityId(jkCommunityId);
                    unit_correlations.setJkbuildunitId(jkUnitId);
                    unit_correlations.setAjbbuildunitId(ajbUnit.getId());
                    unit_correlations.setAjbbuildunitCode(ajbUnitCode);
                    ajbUnits.add(ajbUnit);
                    unitMapper.addAJBUnit(ajbUnit);
                    unitMapper.addUnitCorrelations(unit_correlations);
                    result.put("message", "完成映射!");
                } else {
                    result.put("message", "已存在映射关系,无须再映射!");
                }
            }
        }
        result.put("data", ajbUnits);
        System.out.println(result.get("message"));
        return result;
    }

    /**
     * 根据buildId查询金科单元数据
     * @param buildId
     * @return
     */
    @Override
    public List<JKUnitBase> getJKUnitByBuildId(String buildId) {
        return unitMapper.getJKUnitListByJKBuildingId(buildId);
    }

    /**
     * 根据unitId查询金科单元数据
     * @param unitId
     * @return
     */
    @Override
    public List<JKUnitBase> getJKUnitByUnitId(String unitId) {
        return unitMapper.getJKUnitByUnitId(unitId);
    }

    /**
     * 根据jkUnitId查询映射的安居宝单元数据
     * @param jkUnitId
     * @return
     */
    @Override
    public List<AJBUnit> getAJBUnitByJKUnitId(String jkUnitId) {
        return unitMapper.getAJBUnitByJKUnitId(jkUnitId);
    }

    /**
     * 根据jkBuildId查询映射的安居宝单元数据
     * @param jkBuildId
     * @return
     */
    @Override
    public List<AJBUnit> getAJBUnitListByJKBuildId(String jkBuildId) {
        return unitMapper.getAJBUnitListByJKBuildId(jkBuildId);
    }
}
