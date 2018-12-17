package com.sww.jinke.test.service;

import com.sww.jinke.test.entity.AJBUnit;
import com.sww.jinke.test.entity.JKUnitBase;
import com.sww.jinke.test.util.ResultMapUtil;

import java.util.List;

public interface UnitService {
    ResultMapUtil getJKUnitList();   //一键拉取金科所有单元数据

    ResultMapUtil getJKUnitList(String projectId, String buildId); //根据projectId和buildId拉取金科单元数据

    ResultMapUtil mappedAJBUnit();   //将金科单元数据映射成安居宝单元数据

    List<JKUnitBase> getJKUnitByBuildId(String buildId);

    List<JKUnitBase> getJKUnitByUnitId(String unitId);

    List<AJBUnit> getAJBUnitByJKUnitId(String jkUnitId);

    List<AJBUnit> getAJBUnitListByJKBuildId(String jkBuildId);
}
