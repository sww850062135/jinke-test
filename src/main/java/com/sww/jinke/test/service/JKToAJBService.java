package com.sww.jinke.test.service;

import com.sww.jinke.test.entity.JKBuildingBase;
import com.sww.jinke.test.entity.JKCommunityBase;
import com.sww.jinke.test.entity.JKHouseBase;
import com.sww.jinke.test.entity.JKUnitBase;
import com.sww.jinke.test.util.ResultMapUtil;

import java.util.List;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 15:22 2018/12/12
 */
public interface JKToAJBService {

    ResultMapUtil mappedAJBCommunity(List<JKCommunityBase> jkCommunityBaseList); //映射安居宝小区

    ResultMapUtil mappedAJBBuilding(String jkProjectId, List<JKBuildingBase> jkBuildingBaseList);  //将金科楼栋映射成安居宝楼栋

    ResultMapUtil mappedAJBUnit(String jkBuildId, List<JKUnitBase> jkUnitBaseList);  //将金科单元数据映射成安居宝单元数据

    ResultMapUtil mappedAJBHouse(String jkUnitId, String jkBuildId, List<JKHouseBase> jkHouseBaseList); //将金科房屋映射成安居宝房屋
}
