package com.sww.jinke.test.service.impl;


import com.sww.jinke.test.entity.*;
import com.sww.jinke.test.mapper.JKToAJBMapper;
import com.sww.jinke.test.service.JKToAJBService;
import com.sww.jinke.test.util.DateUtils;
import com.sww.jinke.test.util.ResultMapUtil;
import com.sww.jinke.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 15:28 2018/12/12
 */
@Service
public class JKToAJBServiceImpl implements JKToAJBService{
    private static final Logger logger = LoggerFactory.getLogger(JKToAJBServiceImpl.class);

    @Autowired
    private JKToAJBMapper jkToAJBMapper;

    /**
     * 将金科小区映射成安居宝小区
     * @return
     */
    @Override
    public ResultMapUtil mappedAJBCommunity(List<JKCommunityBase> jkCommunityBaseList) {
        ResultMapUtil result = new ResultMapUtil();
        List<AJBCommunity> communities = new ArrayList<>();
        Community_correlations community_correlations = new Community_correlations();
        logger.info("所要映射的金科小区数据是: "+ jkCommunityBaseList);
        for (int i = 0; i< jkCommunityBaseList.size(); i++){
            AJBCommunity ajbCommunity = new AJBCommunity();
            JKCommunityBase jkCommunityBase = jkCommunityBaseList.get(i);
            String projectId = jkCommunityBase.getProjectId();
            int count = jkToAJBMapper.getCountByJKCommunityId(projectId);
            //如果count小于1,说明该金科小区数据还没有映射成安居宝小区
            if (count<1) {
                //映射安居宝小区
                mappedAJBCommunity(ajbCommunity, jkCommunityBase);
                communities.add(ajbCommunity);
                jkToAJBMapper.addAJBCommunity(ajbCommunity);

                //映射小区关联
                mappedCommunity_correlations(community_correlations,ajbCommunity, jkCommunityBase);
                jkToAJBMapper.addCommunity_correlations(community_correlations);

                result.put("message","映射完成!");
            }else {
                result.put("message", "已存在映射关系，无须再映射");
            }

        }
        result.put("data", communities);
        logger.info("映射完成的安居宝小区数据: " + result.get("data"));
        logger.info("message: " + result.get("message"));
        return result;
    }

    /**
     * 将金科楼栋映射成安居宝楼栋
     * @return
     */
    @Override
    public ResultMapUtil mappedAJBBuilding(String jkProjectId, List<JKBuildingBase> jkBuildingBaseList) {
        ResultMapUtil result = new ResultMapUtil();
        List<AJBBuilding> ajbBuildings = new ArrayList<>();
        Building_correlations building_correlations = new Building_correlations();
        logger.info("所要映射的金科楼栋数据是: " + jkBuildingBaseList);
            for (int i=0; i<jkBuildingBaseList.size(); i++) {
                JKBuildingBase jkBuildingBase = jkBuildingBaseList.get(i);
                AJBBuilding ajbBuilding = new AJBBuilding();
                String jkBuildingId = jkBuildingBase.getBuildId();
                //查询楼栋关联表关联记录,判断该楼栋是否已关联
                int count = jkToAJBMapper.getCountByJKBuildingId(jkBuildingId);
                //若count小于1，则该楼栋没有映射，准备映射
                if (count<1) {
                    //映射楼栋
                    mappedAJBBuilding(jkProjectId, ajbBuilding, i);
                    String ajbCommunityId = ajbBuilding.getCommunityId();
                    if (!StringUtil.isNullOrEmpty(ajbCommunityId)) {
                        ajbBuildings.add(ajbBuilding);
                        //映射楼栋关联
                        mappedBuilding_correlations(building_correlations, jkProjectId, ajbBuilding, jkBuildingId);
                        //插入映射记录
                        jkToAJBMapper.addAJBBuilding(ajbBuilding);
                        jkToAJBMapper.addBuildingCorrelations(building_correlations);
                        result.put("message", "完成映射!");
                        result.put("status", "success");
                        result.put("data", ajbBuildings);
                        logger.info("映射完成的安居宝楼栋数据: " + result.get("data"));
                    }else {
                        List<JKCommunityBase> jkCommunityBaseList = jkToAJBMapper.getJKCommunityByJKProjectId(jkProjectId);
                        result.put("status", "error");
                        result.put("message", "映射失败,该楼栋上的小区还未映射!");
                        result.put("data", jkCommunityBaseList);
                    }
                } else {
                    result.put("message", "已存在映射关系,无须再映射!");
                    result.put("status", "success");
                }
            }
        logger.info("message: " + result.get("message"));
        return result;
    }

    /**
     * 将金科单元数据映射成安居宝单元数据
     * @return
     */
    @Override
    public ResultMapUtil mappedAJBUnit(String jkBuildId, List<JKUnitBase> jkUnitBaseList) {
        ResultMapUtil result = new ResultMapUtil();
        List<AJBUnit> ajbUnits = new ArrayList<>();
        Unit_correlations unit_correlations = new Unit_correlations();
        logger.info("所要映射的金科单元数据是: " + jkUnitBaseList);
            for (int i = 0; i < jkUnitBaseList.size(); i++) {
                JKUnitBase jkUnitBase = jkUnitBaseList.get(i);
                AJBUnit ajbUnit = new AJBUnit();
                String jkUnitId = jkUnitBase.getUnitId();
                //根据jkUnitId查询单元关联表关联记录
                int count = jkToAJBMapper.getCountByJKUnitId(jkUnitId);
                //若count小于1,则该单元无关联记录,可执行映射操作
                if (count < 1) {
                    //映射单元
                    mappedAJBUnit(ajbUnit, jkUnitBase, jkBuildId, i);
                    String ajbCommunityId = ajbUnit.getCommunityId();
                    String ajbBuildingId = ajbUnit.getBuildingId();
                    if ((!StringUtil.isNullOrEmpty(ajbCommunityId)) || (!StringUtil.isNullOrEmpty(ajbBuildingId))) {
                        ajbUnits.add(ajbUnit);
                        //映射单元关联
                        mappedUnit_correlations(unit_correlations, jkUnitBase, jkUnitId, ajbUnit);
                        jkToAJBMapper.addAJBUnit(ajbUnit);
                        jkToAJBMapper.addUnitCorrelations(unit_correlations);
                        result.put("message", "完成映射!");
                        result.put("status", "success");
                        result.put("data", ajbUnits);
                        logger.info("映射完成的安居宝单元数据: " + result.get("data"));
                    }else {
                        String jkProjectId = jkUnitBase.getProjectId();
                        List<JKCommunityBase> jkCommunityBaseList = jkToAJBMapper.getJKCommunityByJKProjectId(jkProjectId);
                        List<JKBuildingBase> jkBuildingBaseList = jkToAJBMapper.getJKBuildingByJKBuildId(jkProjectId, jkBuildId);
                        result.put("status", "error");
                        result.put("message", "映射失败,该单元上的小区或楼栋还未映射!");
                        result.put("data", jkCommunityBaseList);
                        result.put("data1", jkBuildingBaseList);
                    }
                } else {
                    result.put("message", "已存在映射关系,无须再映射!");
                    result.put("status", "success");
                }
            }
        logger.info("message: " + result.get("message"));
        return result;
    }

    /**
     * 将金科房屋映射成安居宝房屋
     * @return
     */
    @Override
    public ResultMapUtil mappedAJBHouse(String jkUnitId, String jkBuildId, List<JKHouseBase> jkHouseBaseList) {
        ResultMapUtil result = new ResultMapUtil();
        List<AJBHouse> ajbHouses = new ArrayList<>();
        House_correlations house_correlations = new House_correlations();
        logger.info("所要映射的金科房屋数据是: " + jkHouseBaseList);
        for (int i = 0; i < jkHouseBaseList.size(); i++) {
            JKHouseBase jkHouseBase = jkHouseBaseList.get(i);
            AJBHouse ajbHouse = new AJBHouse();
            //根据jkHouseId查询房屋关联表关联记录
            int count = jkToAJBMapper.getCountByJKHouseId(jkHouseBase.getHouseId());
            //如果count小于1,则该房屋无关联记录,可执行映射操作
            if (count < 1) {
                //映射房屋
                mappedAJBHouse(jkHouseBase, ajbHouse, jkBuildId, i);
                String ajbCommunityId = ajbHouse.getCommunityId();
                String ajbBuildingId = ajbHouse.getBuildingId();
                String ajbUnitId = ajbHouse.getUnitId();
                if ((!StringUtil.isNullOrEmpty(ajbCommunityId)) || (!StringUtil.isNullOrEmpty(ajbBuildingId)) || (!StringUtil.isNullOrEmpty(ajbUnitId))) {
                    //映射房屋关联实体
                    mappedHouse_correlations(house_correlations, jkHouseBase, ajbHouse);
                    ajbHouses.add(ajbHouse);
                    jkToAJBMapper.addAJBHouse(ajbHouse);
                    jkToAJBMapper.addHouseCorrelations(house_correlations);
                    result.put("status", "success");
                    result.put("message", "完成映射!");
                    result.put("data", ajbHouses);
                    logger.info("映射完成的安居宝房屋数据: " + result.get("data"));
                }else {
                    String jkProjectId = jkHouseBase.getProjectId();
                    List<JKCommunityBase> jkCommunityBaseList = jkToAJBMapper.getJKCommunityByJKProjectId(jkProjectId);
                    List<JKBuildingBase> jkBuildingBaseList = jkToAJBMapper.getJKBuildingByJKBuildId(jkProjectId, jkBuildId);
                    List<JKUnitBase> jkUnitBaseList = jkToAJBMapper.getJKUnitByJKUnitId(jkUnitId);
                    result.put("status", "error");
                    result.put("message", "映射失败,该房屋上的小区或楼栋或单元还未映射!");
                    result.put("data", jkCommunityBaseList);
                    result.put("data1", jkBuildingBaseList);
                    result.put("data2", jkUnitBaseList);
                }
            } else {
                result.put("message", "已存在映射关系,无须再映射!");
                result.put("status", "success");
            }
        }
        logger.info("message: " + result.get("message"));
        return result;
    }

    /**
     * 映射房屋关联实体
     * @param house_correlations
     * @param jkHouseBase
     * @param ajbHouse
     * @return
     */
    private House_correlations mappedHouse_correlations(House_correlations house_correlations, JKHouseBase jkHouseBase, AJBHouse ajbHouse) {
        house_correlations.setId(StringUtil.UUID());
        house_correlations.setJkbuildunitId(jkHouseBase.getUnitId());
        house_correlations.setJkhouseId(jkHouseBase.getHouseId());
        house_correlations.setAjbhosueId(ajbHouse.getId());
        house_correlations.setAjbhouseCode(ajbHouse.getHouseCode());
        return house_correlations;
    }

    /**
     * 映射安居宝房屋实体
     * @param jkHouseBase
     * @param ajbHouse
     * @param jkBuildId
     * @param i
     * @return
     */
    private AJBHouse mappedAJBHouse(JKHouseBase jkHouseBase, AJBHouse ajbHouse, String jkBuildId, int i) {
        String jkCommunityId = jkHouseBase.getProjectId();
        String ajbCommunityId = jkToAJBMapper.getAJBCommunityId(jkCommunityId);
        String ajbBuildingId = jkToAJBMapper.getAJBBuildingId(jkBuildId);
        if ((!StringUtil.isNullOrEmpty(ajbCommunityId)) && (!StringUtil.isNullOrEmpty(ajbBuildingId))) {
            String jkUnitId1 = jkHouseBase.getUnitId();
            int count1 = jkToAJBMapper.getUnitCountByJKBuildId(jkBuildId);
            String ajbUnitCode;
            String ajbUnitId;
            if (!jkUnitId1.isEmpty()) {
                //若金科房屋表中unit_id不为空
                ajbUnitId = jkToAJBMapper.getAJBUnitId(jkUnitId1);
                if (!StringUtil.isNullOrEmpty(ajbUnitId)) {
                    ajbUnitCode = jkToAJBMapper.getAJBUnitCode(ajbUnitId);
                }else {
                    return ajbHouse;
                }
            } else {
                //如果金科房屋表中unit_id为空,则根据 build_id来查找该楼栋下最大的单元数, 新的单元编码在该最大单元数下加1
                ajbUnitId = StringUtil.UUID();
                String ajbBuildingCode = jkToAJBMapper.getAJBBuildingCode(ajbBuildingId);
                if (ajbBuildingCode.length() < 3 && count1 < 10) {
                    ajbUnitCode = ajbBuildingCode + "0" + String.valueOf(count1 + 1);
                } else {
                    ajbUnitCode = ajbBuildingCode + String.valueOf(count1 + 1);
                }
            }
            ajbHouse.setId(StringUtil.UUID());
            ajbHouse.setCommunityId(ajbCommunityId);
            ajbHouse.setBuildingId(ajbBuildingId);
            ajbHouse.setUnitId(ajbUnitId);
            String ajbHouseCode;
            if (i < 9) {
                String code = "000" + String.valueOf(i + 1);
                ajbHouseCode = ajbUnitCode + code;
            } else if (i < 99) {
                String code = "00" + String.valueOf(i + 1);
                ajbHouseCode = ajbUnitCode + code;
            } else if (i < 999) {
                String code = "0" + String.valueOf(i + 1);
                ajbHouseCode = ajbUnitCode + code;
            } else {
                String code = String.valueOf(i + 1);
                ajbHouseCode = ajbUnitCode + code;
            }
            ajbHouse.setHouseCode(ajbHouseCode);
            ajbHouse.setHouseName(ajbHouseCode + "房屋");
            if (i / 2 == 0) {
                ajbHouse.setFloor(2);
            } else {
                ajbHouse.setFloor(1);
            }
            ajbHouse.setCreatedTime(DateUtils.getCurrentDateTime());
            return ajbHouse;
        }else {
            return ajbHouse;
        }
    }

    /**
     * 映射单元关联实体
     * @param unit_correlations
     * @param jkUnitBase
     * @param jkUnitId
     * @param ajbUnit
     * @return
     */
    private Unit_correlations mappedUnit_correlations(Unit_correlations unit_correlations, JKUnitBase jkUnitBase, String jkUnitId, AJBUnit ajbUnit) {
        unit_correlations.setId(StringUtil.UUID());
        unit_correlations.setJkcommunityId(jkUnitBase.getProjectId());
        unit_correlations.setJkbuildunitId(jkUnitId);
        unit_correlations.setAjbbuildunitId(ajbUnit.getId());
        unit_correlations.setAjbbuildunitCode(ajbUnit.getUnitCode());
        return unit_correlations;
    }

    /**
     * 映射安居宝单元实体
     * @param ajbUnit
     * @param jkUnitBase
     * @param jkBuildId
     * @param i
     * @return
     */
    private AJBUnit mappedAJBUnit(AJBUnit ajbUnit, JKUnitBase jkUnitBase, String jkBuildId, int i) {
        String jkCommunityId = jkUnitBase.getProjectId();
        String ajbBuildingId = jkToAJBMapper.getAJBBuildingId(jkBuildId);
        String ajbCommunityId = jkToAJBMapper.getAJBCommunityId(jkCommunityId);
        String ajbBuildingCode = jkToAJBMapper.getAJBBuildingCode(ajbBuildingId);
        if (! StringUtil.isNullOrEmpty(ajbBuildingId)) {
            //映射单元
            ajbUnit.setId(StringUtil.UUID());
            ajbUnit.setCommunityId(ajbCommunityId);
            ajbUnit.setBuildingId(ajbBuildingId);
            String ajbUnitCode;
            String unitNo;
            //4位unit_code 由building_code和单元号unitNo拼接而成
            if (ajbBuildingCode.length() < 3 && i < 9) {
                unitNo = "0" + String.valueOf(i + 1);
                ajbUnitCode = ajbBuildingCode + unitNo;
            } else {
                unitNo = String.valueOf(i + 1);
                ajbUnitCode = ajbBuildingCode + unitNo;
            }
            ajbUnit.setUnitCode(ajbUnitCode);
            ajbUnit.setUnitName(ajbBuildingCode + "楼" + unitNo + "单元");
            ajbUnit.setCreatedTime(DateUtils.getCurrentDateTime());
            return ajbUnit;
        }else {
            return ajbUnit;
        }
    }

    /**
     * 映射小区关联实体方法
     * @param community_correlations 小区关联实体
     * @param ajbCommunity
     * @param jkCommunityBase
     * @return
     */
    private Community_correlations mappedCommunity_correlations(Community_correlations community_correlations, AJBCommunity ajbCommunity, JKCommunityBase jkCommunityBase) {
        community_correlations.setId(StringUtil.UUID());
        community_correlations.setAjbcommunityId(ajbCommunity.getId());
        community_correlations.setJkcommunityId(jkCommunityBase.getProjectId());
        community_correlations.setAjbcommunityCode(ajbCommunity.getCommunityCode());
        return community_correlations;
    }

    /**
     * 映射安居宝小区实体方法
     * @param ajbCommunity 安居宝小区实体
     * @param jkCommunityBase 金科小区实体
     * @return
     */
    private AJBCommunity mappedAJBCommunity(AJBCommunity ajbCommunity, JKCommunityBase jkCommunityBase) {
        ajbCommunity.setId(StringUtil.UUID());
        ajbCommunity.setCommunityCode(StringUtil.RandNumberSex());
        ajbCommunity.setCommunityName(jkCommunityBase.getProjectName());
        ajbCommunity.setProvinceName(jkCommunityBase.getProvinceName());
        ajbCommunity.setCityName(jkCommunityBase.getCityName());
        ajbCommunity.setDistrictName(jkCommunityBase.getDistrictName());
        ajbCommunity.setCreatedTime(DateUtils.getCurrentDateTime());
        return ajbCommunity;
    }

    /**
     * 映射安居宝楼栋实体方法
     * @param jkProjectId
     * @param ajbBuilding
     * @param i
     * @return
     */
    private AJBBuilding mappedAJBBuilding(String jkProjectId, AJBBuilding ajbBuilding, int i) {
        //根据金科小区id在小区关联表中查询安居宝小区id
        String ajbCommunityId = jkToAJBMapper.getAJBCommunityId(jkProjectId);
        //映射楼栋
        ajbBuilding.setId(StringUtil.UUID());
        ajbBuilding.setCommunityId(ajbCommunityId);
        if (i<9) {
            String ajbBuildingCode = "0"+ String.valueOf(i+1);
            ajbBuilding.setBuildingCode(ajbBuildingCode);
        }else {
            String ajbBuildingCode = String.valueOf(i+1);
            ajbBuilding.setBuildingCode(ajbBuildingCode);
        }
        ajbBuilding.setBuildingName(ajbBuilding.getBuildingCode() + "栋");
        ajbBuilding.setCreatedTime(DateUtils.getCurrentDateTime());
        return ajbBuilding;
    }

    /**
     * 映射安居宝楼栋关联实体方法
     * @param building_correlations
     * @param jkProjectId
     * @param ajbBuilding
     */
    private Building_correlations mappedBuilding_correlations(Building_correlations building_correlations, String jkProjectId, AJBBuilding ajbBuilding, String jkBuildingId) {
        building_correlations.setId(StringUtil.UUID());
        building_correlations.setJkcommunityId(jkProjectId);
        building_correlations.setJkbuildingId(jkBuildingId);
        building_correlations.setAjbbuildingId(ajbBuilding.getId());
        return building_correlations;
    }

}
