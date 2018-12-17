package com.sww.jinke.test.mapper;

import com.sww.jinke.test.entity.AJBHouse;
import com.sww.jinke.test.entity.House_correlations;
import com.sww.jinke.test.entity.JKHouseBase;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface HouseMapper {
    @Insert("INSERT INTO jk_house(house_id, house_name, house_type, unit_id, unit_name, " +
            "build_id, build_name, project_id, project_name, project_type, company_name, sync_time)" +
            "VALUES(#{houseId}, #{houseName}, #{houseType}, #{unitId}, #{unitName}, " +
            "#{buildId}, #{buildName}, #{projectId}, #{projectName}, #{projectType}, #{companyName}, #{syncTime})")
    void add(JKHouseBase jkHouseBase);

    @Select("SELECT * FROM jk_house(house_id, house_name, house_type, unit_id, unit_name, build_id, " +
            "build_name, project_id, project_name, project_type, company_name, sync_time) " +
            "VALES(#{houseId},#{houseName},#{houseType},#{unitId},#{unitName},#{buildId},#{buildName}," +
            "#{projectId},#{projectName},#{projectType},#{companyName}, #{syncTime})")
    List<JKHouseBase> getJKHouseList();

    /**
     * 根据jkHouseId查询房屋关联表关联数据
     * @param jkHouseId
     * @return int
     */
    @Select("SELECT COUNT(1) FROM house_correlations WHERE jk_house_id = #{jkhouseId}")
    int getCountByJKHouseId(@Param("jkhouseId") String jkHouseId);


    /**
     * 查询楼栋id列表
     * @return List
     */
    @Select("SELECT DISTINCT build_id FROM jk_house")
    List<String> getJKBuildingIdList();

    /**
     * 根据jkBuildingId查询金科房屋列表
     * @param jkBuildingId 金科楼栋id
     * @return List
     */
    @Select("SELECT * FROM jk_house WHERE build_id = #{buildId}")
    @Results({
            @Result(property = "houseId", column = "house_id"),
            @Result(property = "houseName", column = "house_name"),
            @Result(property = "houseNo", column = "house_no"),
            @Result(property = "houseType", column = "house_type"),
            @Result(property = "unitId", column = "unit_id"),
            @Result(property = "unitName", column = "unit_name"),
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "projectType", column = "project_type"),
            @Result(property = "corp", column = "corp")
    })
    List<JKHouseBase> getJKHouseListByJKBuildingId(String jkBuildingId);

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
     * 根据buildId和unitId查询金科房屋列表
     * @param buildId
     * @param unitId
     * @return
     */
    @Select("SELECT * FROM jk_house WHERE build_id = #{buildId} AND unit_id = #{unitId}")
    @Results({
            @Result(property = "houseId", column = "house_id"),
            @Result(property = "houseName", column = "house_name"),
            @Result(property = "houseNo", column = "house_no"),
            @Result(property = "houseType", column = "house_type"),
            @Result(property = "unitId", column = "unit_id"),
            @Result(property = "unitName", column = "unit_name"),
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "projectType", column = "project_type"),
            @Result(property = "corp", column = "corp")
    })
    List<JKHouseBase> getJKHouseListByBuildIdAndUnitId(@Param("buildId") String buildId, @Param("unitId") String unitId);

    /**
     * 根据houseId查询金科房屋列表
     * @param houseId
     * @return
     */
    @Select("SELECT * FROM jk_house WHERE house_id = #{houseId}")
    @Results({
            @Result(property = "houseId", column = "house_id"),
            @Result(property = "houseName", column = "house_name"),
            @Result(property = "houseNo", column = "house_no"),
            @Result(property = "houseType", column = "house_type"),
            @Result(property = "unitId", column = "unit_id"),
            @Result(property = "unitName", column = "unit_name"),
            @Result(property = "buildId", column = "build_id"),
            @Result(property = "buildName", column = "build_name"),
            @Result(property = "projectId", column = "project_id"),
            @Result(property = "projectName", column = "project_name"),
            @Result(property = "projectType", column = "project_type"),
            @Result(property = "corp", column = "corp")
    })
    List<JKHouseBase> getJKHouseByHouseId(@Param("houseId") String houseId);

    /**
     * 根据jkHouseId查询映射的安居宝房屋数据
     * @param jkHouseId
     * @return
     */
    @Select("SELECT * FROM ajb_house WHERE id = " +
            "(SELECT ajb_house_id FROM house_correlations WHERE jk_house_id = #{jkhouseId})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "communityId", column = "community_id"),
            @Result(property = "buildingId", column = "building_id"),
            @Result(property = "unitId", column = "unit_id"),
            @Result(property = "houseCode", column = "house_code"),
            @Result(property = "houseName", column = "house_name"),
            @Result(property = "floor", column = "floor"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<AJBHouse> getAJBHouseByJKHouseId(@Param("jkhouseId") String jkHouseId);

    /**
     * 根据jkUnitId查询映射的安居宝房屋列表
     * @param jkUnitId
     * @return
     */
    @Select("SELECT * FROM ajb_house WHERE unit_id = " +
            "(SELECT ajb_build_unit_id FROM unit_correlations WHERE jk_build_unit_id = #{jkbuildunitId})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "communityId", column = "community_id"),
            @Result(property = "buildingId", column = "building_id"),
            @Result(property = "unitId", column = "unit_id"),
            @Result(property = "houseCode", column = "house_code"),
            @Result(property = "houseName", column = "house_name"),
            @Result(property = "floor", column = "floor"),
            @Result(property = "createdTime", column = "created_time"),
            @Result(property = "updateTime", column = "update_time")
    })
    List<AJBHouse> getAJBHouseListByJKUnitId(@Param("jkbuildunitId") String jkUnitId);

    /**
     * 根据jkHouseId查询安居宝房屋编码
     * @param jkHouseId
     * @return
     */
    @Select("SELECT ajb_house_code FROM house_correlations WHERE jk_house_id = #{jkhouseId}")
    String getAJBHouseCode(@Param("jkhouseId") String jkHouseId);
}
