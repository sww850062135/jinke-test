package com.sww.jinke.test.service;


import com.sww.jinke.test.entity.AJBCommunity;
import com.sww.jinke.test.util.ResultMapUtil;
import com.sww.jinke.test.entity.JKCommunityBase;


import java.util.List;

public interface CommunityService {

    ResultMapUtil getJKCommunityList(String companyId);   //根据companyId拉取金科小区数据

    List<JKCommunityBase> selectJKCommunityList(int pageNum, int pageSize) throws Exception;   //分页查询金科小区列表

    List<JKCommunityBase> selectJKCommunityByProjectId(String projectId);     //根据金科小区项目Id查询


    ResultMapUtil mappedAJBCommunityList(); //映射安居宝小区


    List<JKCommunityBase> selectJKCommunityList();

    List<AJBCommunity> selectAJBCommunityByProjectId(String projectId);

    List<AJBCommunity> selectAJBCommunityList(int pageNum, int pageSize) throws Exception;

    String selectAJBCommunityCode(String projectId);
}
