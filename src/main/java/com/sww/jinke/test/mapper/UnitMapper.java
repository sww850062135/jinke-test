package com.sww.jinke.test.mapper;

import com.sww.jinke.test.entity.AJBUnit;
import com.sww.jinke.test.entity.JKUnitBase;
import com.sww.jinke.test.entity.Unit_correlations;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UnitMapper {
    @Insert("INSERT INTO jk_unit(unit_id, unit_name, build_id, build_name, project_id, project_name, sync_time)" +
            "VALUES(#{unitId}, #{unitName}, #{buildId}, #{buildName}, #{projectId}, #{projectName}, #{syncTime})")
    void add(JKUnitBase JKUnitBase);


    @Select("SELECT * FROM jk_unit")
    @Results({
            @Result(property = "unitId", column = "unit_id"),
            @Result(property = "unitName", column = "unit_name"),
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
    })
    List<JKUnitBase> getJKUnitList();

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
     * 根据jkUnitId 查询 ajbUnitId
     * @param jkUnitId 金科单元id
     * @return ajbUnitId
     */
    @Select("SELECT ajb_build_unit_id FROM unit_correlations WHERE jk_build_unit_id = #{jkbuildunitId}")
    String getAJBUnitId(@Param("jkbuildunitId") String jkUnitId);

    /**
     * 查询build_id列表
     * @return List
     */
    @Select("SELECT DISTINCT build_id FROM jk_unit")
    List<String> getJKBuildingIdList();

    /**
     * 根据build_id查询金科单元信息
     * @param jkBuildingId 金科楼栋id
     * @return List
     */
    @Select("SELECT * FROM jk_unit WHERE build_id = #{buildId}")
    @Results({
            @Result(property = "unitId", column = "unit_id"),
            @Result(property = "unitName", column = "unit_name"),
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "syncTime", column = "sync_time")
    })
    List<JKUnitBase> getJKUnitListByJKBuildingId(@Param("buildId") String jkBuildingId);

    /**
     * 根据ajbUnitId查询ajbUnitCode
     * @param ajbUnitId 安居宝单元id
     * @return
     */
    @Select("SELECT ajb_build_unit_code FROM unit_correlations WHERE ajb_build_unit_id = #{ajbbuildunitId}")
    String getAJBUnitCode(@Param("ajbbuildunitId") String ajbUnitId);

    /**
     * 根据jkBuildingId查询单元总数
     * @param jkBuildingId 金科楼栋id
     * @return int
     */
    @Select("SELECT COUNT(1) FROM jk_unit WHERE build_id = #{buildId}")
    int getUnitCountByJKBuildId(@Param("buildId") String jkBuildingId);

    /**
     * 根据unitId查询金科单元数据
     * @param unitId
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
    List<JKUnitBase> getJKUnitByUnitId(@Param("unitId") String unitId);

    /**
     * 根据jkUnitId查询映射的安居宝单元数据
     * @param jkUnitId
     * @return
     */
    @Select("SELECT * FROM ajb_unit WHERE id = " +
            "(SELECT ajb_build_unit_id FROM unit_correlations WHERE jk_build_unit_id = #{jkbuildunitId})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "communityId", column = "community_id"),
            @Result(property = "buildingId", column = "building_id"),
            @Result(property = "unitName", column = "unit_name"),
            @Result(property = "unitCode", column = "unit_code"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<AJBUnit> getAJBUnitByJKUnitId(@Param("jkbuildunitId") String jkUnitId);

    /**
     * 根据jkBuildId查询映射的安居宝单元数据
     * @param jkBuildId
     * @return
     */
    @Select("SELECT * FROM ajb_unit WHERE building_id = " +
            "(SELECT ajb_building_id FROM building_correlations WHERE jk_building_id = #{jkbuildingId})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "communityId", column = "community_id"),
            @Result(property = "buildingId", column = "building_id"),
            @Result(property = "unitName", column = "unit_name"),
            @Result(property = "unitCode", column = "unit_code"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<AJBUnit> getAJBUnitListByJKBuildId(@Param("jkbuildingId") String jkBuildId);
}
