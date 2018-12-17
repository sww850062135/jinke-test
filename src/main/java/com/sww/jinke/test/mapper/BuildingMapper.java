package com.sww.jinke.test.mapper;

import com.sww.jinke.test.entity.AJBBuilding;
import com.sww.jinke.test.entity.Building_correlations;
import com.sww.jinke.test.entity.JKBuildingBase;
import org.apache.ibatis.annotations.*;

import java.util.List;
/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 10:40 2018/11/12
 */
public interface BuildingMapper {

    /**
     * 同步金科楼栋数据到数据库
     * @param jkBuildingBase 金科楼栋实体
     */
    @Insert("INSERT INTO jk_building(build_id, build_name, project_id, project_name, sync_time)" +
            "VALUES(#{buildId}, #{buildName}, #{projectId}, #{projectName}, #{syncTime})")
    void add(JKBuildingBase jkBuildingBase);


    /**
     * 根据 jkBuildingId 查询楼栋关联记录
     * @param jkBuildingId 金科楼栋id
     * @return int
     */
    @Select("SELECT COUNT(1) FROM building_correlations WHERE jk_building_id = #{buildId}")
    int getCountByJKBuildingId(@Param("buildId") String jkBuildingId);

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
     * 根据 jkBuildID 查询 ajb_building_id
     * @param jkBuildingId 金科楼栋id
     * @return ajb_building_id
     */
    @Select("SELECT ajb_building_id FROM building_correlations WHERE jk_building_id = #{jkbuildingId}")
    String getAJBBuildingId(@Param("jkbuildingId") String jkBuildingId);

    /**
     * 根据 安居宝楼栋id 查询安居宝楼栋编码 building_code
     * @param ajbBuildingId 安居宝楼栋id
     * @return building_code
     */
    @Select("SELECT building_code FROM ajb_building WHERE id = #{id}")
    String getAJBBuildingCode(@Param("id") String ajbBuildingId);

    /**
     * 根据金科小区id查询金科楼栋列表
     * @param projectId 金科小区id
     * @return jkBuildingBaseList
     */
    @Select("SELECT * FROM jk_building WHERE project_id = #{projectId}")
    @Results({
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "syncTime", column = "sync_time")
    })
    List<JKBuildingBase> getJKBuildingListByProjectId(@Param("projectId") String projectId);

    /**
     * 查询金科小区id列表
     * @return
     */
    @Select("SELECT DISTINCT project_id FROM jk_building")
    List<String> getJKProjectId();

    /**
     * 根据金科小区id和楼栋id查询楼栋数据
     * @param projectId
     * @param buildId
     * @return
     */
    @Select("SELECT * FROM jk_building WHERE project_id = #{project_id} AND build_id = #{build_id}")
    @Results({
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "syncTime", column = "sync_time")
    })
    List<JKBuildingBase> getJKBuildByBuildId(@Param("project_id") String projectId, @Param("build_id") String buildId);

    /**
     * 根据projectId查询安居宝楼栋数据
     * @param projectId
     * @return
     */
    @Select("SELECT * FROM ajb_building WHERE community_id = " +
            "(SELECT ajb_community_id FROM community_correlations WHERE jk_community_id = #{jkcommunityId})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "communityId", column = "community_id"),
            @Result(property = "buildingCode", column = "building_code"),
            @Result(property = "buildingName", column = "building_name"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<AJBBuilding> getAJBBuildByProjectId(@Param("jkcommunityId") String projectId);

    /**
     * 根据jkBuildId查询安居宝楼栋数据
     * @param jkBuildId
     * @return
     */
    @Select("SELECT * FROM ajb_building WHERE id = " +
            "(SELECT ajb_building_id FROM building_correlations WHERE jk_building_id = #{jkbuildingId})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "communityId", column = "community_id"),
            @Result(property = "buildingCode", column = "building_code"),
            @Result(property = "buildingName", column = "building_name"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<AJBBuilding> getAJBBuildByJKBuildId(@Param("jkbuildingId") String jkBuildId);
}
