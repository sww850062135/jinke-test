package com.sww.jinke.test.mapper;

import com.sww.jinke.test.entity.AJBCommunity;
import com.sww.jinke.test.entity.JKCommunityBase;
import com.sww.jinke.test.entity.Community_correlations;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CommunityMapper {

    /**
     * 将拉取得到的金科小区数据保存到数据库
     * @param jkCommunityBase
     * @return
     */
    @Insert("INSERT INTO jk_community(project_id, project_name, province_name, city_name, district_name," +
            "company_id, company_name, house_total, house_num, sync_time, sync_status, sync_msg) VALUES (#{projectId}, #{projectName}," +
            "#{provinceName}, #{cityName}, #{districtName}, #{companyId}, #{companyName}," +
            "#{houseTotal}, #{houseNum}, #{syncTime}, #{syncStatus}, #{syncMsg})")
    int add(JKCommunityBase jkCommunityBase);

    /**
     * 查询金科小区id列表
     * @return
     */
    @Select("Select project_id from jk_community")
    List<String> getJKCommunityIdList();

    /**
     * 查询金科小区列表
     * @return
     */
    @Select("SELECT * FROM jk_community")
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
    List<JKCommunityBase> getJKcommunitylist();


    /**
     * 根据项目Id查询金科小区数据
     * @param projectId 项目名称
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
    List<JKCommunityBase> getJKcommunityByProjectId(@Param("projectId") String projectId);


    /**
     * 插入由金科小区转化的安居宝小区
     * @param ajbCommunity
     */
    @Insert("INSERT INTO ajb_community(id,community_code,community_name,province_id,province_name,city_id, city_name, district_id, district_name," +
            "created_time,update_time) VALUES (#{id},#{communityCode},#{communityName},#{provinceId},#{provinceName}," +
            "#{cityId},#{cityName},#{districtId},#{districtName},#{createdTime},#{updateTime})")
    int addAJBCommunity(AJBCommunity ajbCommunity);


    /**
     * 根据 jk_community_id 查询小区关联记录
     * @param jk_community_id 金科小区id
     * @return int
     */
    @Select("SELECT COUNT(1) FROM community_correlations WHERE jk_community_id = #{jk_community_id}")
    int getCountByjk_community_id(@Param("jk_community_id") String jk_community_id);
    /**
     * 插入小区关联实体数据
     * @param community_correlations 小区关联实体
     */
    @Insert("INSERT INTO community_correlations(id,jk_community_id,ajb_community_id,ajb_community_code) " +
            "VALUES(#{id},#{jkcommunityId},#{ajbcommunityId},#{ajbcommunityCode})")
    void addCommunity_correlations(Community_correlations community_correlations);

    /**
     * 根据金科小区id在小区关联表中查询安居宝小区id
     * @param jkCommunityId 金科小区id
     * @return
     */
    @Select("SELECT ajb_community_id FROM community_correlations WHERE jk_community_id IN" +
            "(SELECT DISTINCT project_id FROM jk_building WHERE project_id = #{projectId})")
    String getAJBCommunityId(@Param("projectId") String jkCommunityId);

    /**
     * 根据金科项目ID查询安居宝小区
     * @param projectId
     * @return
     */
    @Select("SELECT * FROM ajb_community WHERE id IN " +
            "(SELECT ajb_community_id FROM community_correlations WHERE jk_community_id = #{projectId})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "communityCode", column = "community_code"),
            @Result(property = "communityName", column = "community_name"),
            @Result(property = "provinceName", column = "province_name"),
            @Result(property = "cityName", column = "city_name"),
            @Result(property = "districtName", column = "district_name"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "updateTime", column = "update_time"),
    })
    List<AJBCommunity> getAJBCommunityByProjectId(@Param("projectId") String projectId);

    /**
     * 查询安居宝小区数据列表
     * @return
     */
    @Select("SELECT * FROM ajb_community")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "communityCode", column = "community_code"),
            @Result(property = "communityName", column = "community_name"),
            @Result(property = "provinceName", column = "province_name"),
            @Result(property = "cityName", column = "city_name"),
            @Result(property = "districtName", column = "district_name"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<AJBCommunity> getAJBCommunityList();

    /**
     * 根据金科项目ID查询安居宝小区编码
     * @param projectId
     * @return
     */
    @Select("SELECT ajb_community_code FROM community_correlations WHERE jk_community_id = #{jkcommunityId}")
    String getAJBCommunityCode(@Param("jkcommunityId") String projectId);
}
