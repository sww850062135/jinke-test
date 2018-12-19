package com.sww.jinke.test.mapper;

import com.sww.jinke.test.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 15:38 2018/12/12
 */
public interface JKToAJBMapper {

    /**
     * 根据 jk_community_id 查询小区关联记录
     * @param jk_community_id 金科小区id
     * @return int
     */
    @Select("SELECT COUNT(1) FROM community_correlations WHERE jk_community_id = #{jk_community_id}")
    int getCountByJKCommunityId(@Param("jk_community_id") String jk_community_id);

    /**
     * 插入由金科小区转化的安居宝小区
     * @param ajbCommunity
     */
    @Insert("INSERT INTO ajb_community(id,community_code,community_name,province_name, city_name, district_name," +
            "created_time,update_time) VALUES (#{id},#{communityCode},#{communityName},#{provinceName}," +
            "#{cityName},#{districtName},#{createdTime},#{updateTime})")
    int addAJBCommunity(AJBCommunity ajbCommunity);

    /**
     * 插入小区关联实体数据
     * @param community_correlations 小区关联实体
     */
    @Insert("INSERT INTO community_correlations(id,jk_community_id,ajb_community_id,ajb_community_code) " +
            "VALUES(#{id},#{jkcommunityId},#{ajbcommunityId},#{ajbcommunityCode})")
    void addCommunity_correlations(Community_correlations community_correlations);

    /**
     * 根据 jkBuildingId 查询楼栋关联记录
     * @param jkBuildingId 金科楼栋id
     * @return int
     */
    @Select("SELECT COUNT(1) FROM building_correlations WHERE jk_building_id = #{buildId}")
    int getCountByJKBuildingId(@Param("buildId") String jkBuildingId);

    /**
     * 根据金科小区id在小区关联表中查询安居宝小区id
     * @param jkCommunityId 金科小区id
     * @return
     */
    @Select("SELECT ajb_community_id FROM community_correlations WHERE jk_community_id IN" +
            "(SELECT DISTINCT project_id FROM jk_building WHERE project_id = #{projectId})")
    String getAJBCommunityId(@Param("projectId") String jkCommunityId);

    /**
     * 将金科楼栋映射成安居宝楼栋
     * @param ajbBuilding 安居宝楼栋
     */
    @Insert("INSERT INTO ajb_building(id, community_id, building_code, building_name, created_time, update_time)" +
            "VALUES(#{id}, #{communityId}, #{buildingCode}, #{buildingName}, #{createdTime}, #{updateTime})")
    void addAJBBuilding(AJBBuilding ajbBuilding);

    /**
     * 插入楼栋关联实体
     * @param building_correlations 楼栋关联实体
     */
    @Insert("INSERT INTO building_correlations(id, jk_community_id, jk_building_id, ajb_building_id)" +
            "VALUES(#{id},#{jkcommunityId},#{jkbuildingId},#{ajbbuildingId})")
    void addBuildingCorrelations(Building_correlations building_correlations);

    /**
     * 根据 jkBuildId 查询 ajb_building_id
     * @param jkBuildId 金科楼栋id
     * @return ajb_building_id
     */
    @Select("SELECT ajb_building_id FROM building_correlations WHERE jk_building_id = #{jkbuildingId}")
    String getAJBBuildingId(@Param("jkbuildingId") String jkBuildId);

    /**
     * 根据 安居宝楼栋id 查询安居宝楼栋编码 building_code
     * @param ajbBuildingId 安居宝楼栋id
     * @return building_code
     */
    @Select("SELECT building_code FROM ajb_building WHERE id = #{id}")

    String getAJBBuildingCode(@Param("id") String ajbBuildingId);
    /**
     * 根据jkUnitId查询单元关联表关联记录
     * @param jkUnitId 金科单元id
     * @return
     */
    @Select("SELECT COUNT(1) FROM unit_correlations WHERE jk_build_unit_id = #{unitId}")
    int getCountByJKUnitId(@Param("unitId") String jkUnitId);

    /**
     * 插入映射的安居宝单元
     * @param ajbUnit 安居宝单元
     */
    @Insert("INSERT INTO ajb_unit(id, community_id, building_id, unit_name, unit_code, created_time, update_time)" +
            "VALUES(#{id}, #{communityId}, #{buildingId}, #{unitName}, #{unitCode}, #{createdTime}, #{updateTime})")
    void addAJBUnit(AJBUnit ajbUnit);

    /**
     * 插入映射的单元关联实体
     * @param unit_correlations 单元关联实体
     */
    @Insert("INSERT INTO unit_correlations(id, jk_community_id, jk_build_unit_id, ajb_build_unit_id, ajb_build_unit_code)" +
            "VALUES(#{id}, #{jkcommunityId}, #{jkbuildunitId}, #{ajbbuildunitId}, #{ajbbuildunitCode})")
    void addUnitCorrelations(Unit_correlations unit_correlations);

    /**
     * 根据jkBuildId查询单元总数
     * @param jkBuildId 金科楼栋id
     * @return int
     */
    @Select("SELECT COUNT(1) FROM jk_unit WHERE build_id = #{buildId}")
    int getUnitCountByJKBuildId(@Param("buildId") String jkBuildId);

    /**
     * 根据jkUnitId 查询 ajbUnitId
     * @param jkUnitId 金科单元id
     * @return ajbUnitId
     */
    @Select("SELECT ajb_build_unit_id FROM unit_correlations WHERE jk_build_unit_id = #{jkbuildunitId}")
    String getAJBUnitId(@Param("jkbuildunitId") String jkUnitId);

    /**
     * 根据ajbUnitId查询ajbUnitCode
     * @param ajbUnitId 安居宝单元id
     * @return
     */
    @Select("SELECT ajb_build_unit_code FROM unit_correlations WHERE ajb_build_unit_id = #{ajbbuildunitId}")
    String getAJBUnitCode(@Param("ajbbuildunitId") String ajbUnitId);

    /**
     * 根据jkHouseId查询房屋关联表关联数据
     * @param houseId
     * @return int
     */
    @Select("SELECT COUNT(1) FROM house_correlations WHERE jk_house_id = #{jkhouseId}")
    int getCountByJKHouseId(@Param("jkhouseId") String houseId);

    /**
     * 插入映射的安居宝房屋数据
     * @param ajbHouse
     */
    @Insert("INSERT INTO ajb_house(id, community_id, building_id, unit_id, house_code, house_name, floor, created_time, update_time) " +
            "VALUES(#{id},#{communityId},#{buildingId},#{unitId},#{houseCode},#{houseName},#{floor},#{createdTime},#{updateTime})")
    void addAJBHouse(AJBHouse ajbHouse);

    /**
     * 插入房屋关联实体
     * @param house_correlations
     */
    @Insert("INSERT INTO house_correlations(id, jk_build_unit_id, jk_house_id, ajb_house_id, ajb_house_code)" +
            "VALUES(#{id}, #{jkbuildunitId}, #{jkhouseId}, #{ajbhosueId}, #{ajbhouseCode})")
    void addHouseCorrelations(House_correlations house_correlations);

    /**
     * 根据项目Id查询金科小区数据
     * @param jkProjectId 项目名称
     * @return List
     */
    @Select("SELECT * FROM jk_community WHERE project_id = #{projectId}")
    @Results({
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "provinceName", column = "province_name"),
            @Result(property = "cityName", column = "city_name"),
            @Result(property = "districtName", column = "district_name"),
            @Result(property = "companyId", column = "company_id"),
            @Result(property = "companyName", column = "company_name"),
            @Result(property = "houseTotal", column = "house_total"),
            @Result(property = "houseNum", column = "house_num"),
            @Result(property = "syncTime", column = "sync_time"),
            @Result(property = "syncStatus", column = "sync_status"),
            @Result(property = "syncMsg", column = "sync_msg")
    })
    List<JKCommunityBase> getJKCommunityByJKProjectId(@Param("projectId") String jkProjectId);

    /**
     * 根据金科小区id和楼栋id查询楼栋数据
     * @param jkProjectId
     * @param jkBuildId
     * @return
     */
    @Select("SELECT * FROM jk_building WHERE project_id = #{projectId} AND build_id = #{buildId}")
    @Results({
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "syncTime", column = "sync_time")
    })
    List<JKBuildingBase> getJKBuildingByJKBuildId(@Param("projectId") String jkProjectId, @Param("buildId") String jkBuildId);

    /**
     * 根据jkUnitId查询金科单元数据
     * @param jkUnitId
     * @return
     */
    @Select("SELECT * FROM jk_unit WHERE unit_id = #{unitId}")
    @Results({
            @Result(property = "unitId", column = "unit_id"),
            @Result(property = "unitName", column = "unit_name"),
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "syncTime", column = "sync_time")
    })
    List<JKUnitBase> getJKUnitByJKUnitId(@Param("unitId") String jkUnitId);
}
