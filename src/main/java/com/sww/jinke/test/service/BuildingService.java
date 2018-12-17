package com.sww.jinke.test.service;

import com.sww.jinke.test.entity.AJBBuilding;
import com.sww.jinke.test.entity.JKBuildingBase;
import com.sww.jinke.test.util.ResultMapUtil;

import java.util.List;

public interface BuildingService {

    ResultMapUtil getJKBuildingList();  //一键拉取金科所有楼栋数据

    ResultMapUtil getJKBuildingList(String projectId);  //根据projectId拉取金科楼栋数据

    ResultMapUtil mappedAJBBuilding();  //映射安居宝楼栋

    List<JKBuildingBase> getJKBuildingByProjectId(String projectId);

    List<JKBuildingBase> getJKBuildByBuildId(String projectId, String buildId);

    List<AJBBuilding> getAJBBuildByProjectId(String projectId);

    List<AJBBuilding> getAJBBuildByJKBuildId(String jkBuildId);
}
