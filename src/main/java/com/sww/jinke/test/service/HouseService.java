package com.sww.jinke.test.service;

import com.sww.jinke.test.entity.AJBHouse;
import com.sww.jinke.test.entity.JKHouseBase;
import com.sww.jinke.test.util.ResultMapUtil;

import java.util.List;

public interface HouseService {
    ResultMapUtil getJKHouseList(); //一键拉取金科所有房屋数据

    ResultMapUtil getJKHouseList(String projectId, String buildId, String unitId); //根据projectId,buildId和unitId拉取金科房屋数据

    ResultMapUtil mappedAJBHouse(); //将金科房屋数据映射成安居宝房屋数据

    List<JKHouseBase> getJKHouseListByBuildIdAndUnitId(String buildId, String unitId);

    List<JKHouseBase> getJKHouseByHouseId(String houseId);

    List<AJBHouse> getAJBHouseByJKHouseId(String jkHouseId);

    List<AJBHouse> selectAJBHouseListByJKUnitId(String jkUnitId);

    String selectAJBHouseCode(String jkHouseId);
}
