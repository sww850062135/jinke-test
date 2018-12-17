package com.sww.jinke.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sww.jinke.test.entity.AJBHouse;
import com.sww.jinke.test.entity.House_correlations;
import com.sww.jinke.test.mapper.BuildingMapper;
import com.sww.jinke.test.mapper.CommunityMapper;
import com.sww.jinke.test.mapper.HouseMapper;
import com.sww.jinke.test.mapper.UnitMapper;
import com.sww.jinke.test.service.HouseService;
import com.sww.jinke.test.util.*;
import com.sww.jinke.test.entity.JKHouseBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class HouseServiceImpl implements HouseService{

    private static final Logger logger = LoggerFactory.getLogger(HouseServiceImpl.class);

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private BuildingMapper buildingMapper;

    @Autowired
    private UnitMapper unitMapper;

    /**
     * 一键拉取金科所有房屋数据
     * @return Map
     */
    @Override
    public ResultMapUtil getJKHouseList() {
        ResultMapUtil result = new ResultMapUtil();
        List<String> projectIdList = communityMapper.getJKCommunityIdList();
        for (int i = 0; i < projectIdList.size(); i++) {
            try {
                HashMap<String, Object> map;
                map= SignUtils.param();
                String projectId = projectIdList.get(i);
                map.put("projectId", projectId);
                String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.gethouselist");
                result = HttpUtils.URLPost(url, map);
                logger.info("返回的结果集: " + result);
                JSONObject jsonObject = (JSONObject) JSONObject.parse(String.valueOf(result.get("data")));
                JSONArray jsonArray = JSONObject.parseArray(String.valueOf(jsonObject.get("data")));
                if (jsonArray.size() == 0) {
                    logger.info("projectId:" + projectId + " 该项目下无基础数据!");
                }else {
                    List<JKHouseBase> jkHouseBaseList = new ArrayList<>();
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JKHouseBase jkHouseBase = JSON.parseObject(jsonArray.getString(j), JKHouseBase.class);
                        jkHouseBaseList.add(jkHouseBase);
                        //插入到数据库中
                        houseMapper.add(jkHouseBase);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据projectId,buildId和unitId拉取金科房屋数据
     * @return Map
     */
    @Override
    public ResultMapUtil getJKHouseList(String projectId, String buildId, String unitId) {
        ResultMapUtil result = new ResultMapUtil();
        try {
            HashMap<String, Object> map;
            map = SignUtils.param();
            map.put("projectId", projectId);
            map.put("buildId", buildId);
            if (StringUtil.isNullOrEmpty(buildId)) {
                map.remove("buildId");
            }
            map.put("unitId", unitId);
            if (StringUtil.isNullOrEmpty(unitId)) {
                map.remove("unitId");
            }
            String url = PropertyUtil.getProperty("jinke.api") + PropertyUtil.getProperty("jinke.api.gethouselist");
            result = HttpUtils.URLPost(url, map);
            logger.info("返回的结果集: " + result);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(String.valueOf(result.get("data")));
            JSONArray jsonArray = JSONObject.parseArray(String.valueOf(jsonObject.get("data")));
            if (jsonArray.size() == 0) {
                logger.info("projectId: " + projectId + "buildId: " + buildId + "unitId: " + unitId + "该小区楼栋单元下无基础数据!");
            } else {
                List<JKHouseBase> jkHouseBaseList = new ArrayList<>();
                for (int j = 0; j < jsonArray.size(); j++) {
                    JKHouseBase jkHouseBase = JSON.parseObject(jsonArray.getString(j), JKHouseBase.class);
                    jkHouseBaseList.add(jkHouseBase);
                    //插入到数据库中
                    houseMapper.add(jkHouseBase);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将金科房屋映射成安居宝房屋
     * @return
     */
    @Override
    public ResultMapUtil mappedAJBHouse() {
        ResultMapUtil result = new ResultMapUtil();
        List<AJBHouse> ajbHouses = new ArrayList<>();
        House_correlations house_correlations = new House_correlations();
        List<JKHouseBase> jkHouseBaseList;
        List<String> jkBuildingIdList = houseMapper.getJKBuildingIdList();
        for (int i=0; i<jkBuildingIdList.size(); i++) {
            String jkBuildingId = jkBuildingIdList.get(i);
            jkHouseBaseList = houseMapper.getJKHouseListByJKBuildingId(jkBuildingId);
            for (int j=0; j<jkHouseBaseList.size(); j++) {
                JKHouseBase jkHouseBase = jkHouseBaseList.get(j);
                AJBHouse ajbHouse = new AJBHouse();
                String jkCommunityId = jkHouseBase.getProjectId();
                String ajbCommunityId = communityMapper.getAJBCommunityId(jkCommunityId);
                String ajbBuildingId = buildingMapper.getAJBBuildingId(jkBuildingId);
                String jkUnitId = jkHouseBase.getUnitId();
                int count1 = unitMapper.getUnitCountByJKBuildId(jkBuildingId);
                String ajbUnitCode;
                String ajbUnitId;
                if (!jkUnitId.isEmpty()) {
                    ajbUnitId = unitMapper.getAJBUnitId(jkUnitId);
                    ajbUnitCode = unitMapper.getAJBUnitCode(ajbUnitId);
                }else { //若金科房屋表中unit_id为空,则根据 build_id来查找该楼栋下最大的单元数, 新的单元编码在该最大单元数下加1
                    ajbUnitId = StringUtil.UUID();
                    String ajbBuildingCode = buildingMapper.getAJBBuildingCode(ajbBuildingId);
                    if (ajbBuildingCode.length()<3 && count1<10) {
                        ajbUnitCode = ajbBuildingCode + "0" + String.valueOf(count1+1);
                    }else {
                     ajbUnitCode = ajbBuildingCode + String.valueOf(count1+1);
                    }
                }
                //根据jkHouseId查询房屋关联表关联记录
                int count = houseMapper.getCountByJKHouseId(jkHouseBase.getHouseId());
                //如果count小于1,则该房屋无关联记录,可执行映射操作
                if (count < 1) {
                    //映射房屋
                    ajbHouse.setId(StringUtil.UUID());
                    ajbHouse.setCommunityId(ajbCommunityId);
                    ajbHouse.setBuildingId(ajbBuildingId);
                    ajbHouse.setUnitId(ajbUnitId);
                    String ajbHouseCode;
                    if (j + 1 < 10) {
                        String code = "000" + String.valueOf(j + 1);
                        ajbHouseCode = ajbUnitCode + code;
                    } else if (j + 1 < 100) {
                        String code = "00" + String.valueOf(j + 1);
                        ajbHouseCode = ajbUnitCode + code;
                    } else if (j + 1 < 1000) {
                        String code = "0" + String.valueOf(j + 1);
                        ajbHouseCode = ajbUnitCode + code;
                    } else {
                        String code = String.valueOf(j + 1);
                        ajbHouseCode = ajbUnitCode + code;
                    }
                    ajbHouse.setHouseCode(ajbHouseCode);
                    ajbHouse.setHouseName(ajbHouseCode + "房屋");
                    if (j / 2 == 0) {
                        ajbHouse.setFloor(2);
                    } else {
                        ajbHouse.setFloor(1);
                    }
                    ajbHouse.setCreatedTime(DateUtils.getCurrentDateTime());
                    //映射房屋关联实体
                    house_correlations.setId(StringUtil.UUID());
                    house_correlations.setJkbuildunitId(jkUnitId);
                    house_correlations.setJkhouseId(jkHouseBase.getHouseId());
                    house_correlations.setAjbhosueId(ajbHouse.getId());
                    house_correlations.setAjbhouseCode(ajbHouseCode);
                    ajbHouses.add(ajbHouse);
                    houseMapper.addAJBHouse(ajbHouse);
                    houseMapper.addHouseCorrelations(house_correlations);
                    result.put("message", "完成映射!");
                } else {
                    result.put("message", "已存在映射关系,无须再映射!");
                }
            }
        }
        result.put("data", ajbHouses);
        System.out.println(result.get("message"));
        return result;

    }

    /**
     * 根据buildId和unitId查询金科房屋列表
     * @param buildId
     * @param unitId
     * @return
     */
    @Override
    public List<JKHouseBase> getJKHouseListByBuildIdAndUnitId(String buildId, String unitId) {
        return houseMapper.getJKHouseListByBuildIdAndUnitId(buildId, unitId);
    }

    /**
     * 根据houseId查询金科房屋列表
     * @param houseId
     * @return
     */
    @Override
    public List<JKHouseBase> getJKHouseByHouseId(String houseId) {
        return houseMapper.getJKHouseByHouseId(houseId);
    }

    /**
     * 根据jkHouseId查询映射的安居宝房屋数据
     * @param jkHouseId
     * @return
     */
    @Override
    public List<AJBHouse> getAJBHouseByJKHouseId(String jkHouseId) {
        return houseMapper.getAJBHouseByJKHouseId(jkHouseId);
    }

    /**
     * 根据jkUnitId查询映射的安居宝房屋列表
     * @param jkUnitId
     * @return
     */
    @Override
    public List<AJBHouse> selectAJBHouseListByJKUnitId(String jkUnitId) {
        return houseMapper.getAJBHouseListByJKUnitId(jkUnitId);
    }

    /**
     * 根据jkHouseId查询安居宝房屋编码
     * @param jkHouseId
     * @return
     */
    @Override
    public String selectAJBHouseCode(String jkHouseId) {
        return houseMapper.getAJBHouseCode(jkHouseId);
    }


}
