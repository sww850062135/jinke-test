package com.sww.jinke.test.controller;


import com.sww.jinke.test.entity.AJBBuilding;
import com.sww.jinke.test.entity.JKBuildingBase;
import com.sww.jinke.test.entity.JKCommunityBase;
import com.sww.jinke.test.entity.JsonResult;
import com.sww.jinke.test.service.BuildingService;
import com.sww.jinke.test.util.ResultMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 10:31 2018/11/12
 */
@RestController
@RequestMapping(value = "/api/base")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    /**
     * 拉取金科楼栋数据
     * @return
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/getJKbuildlist", method = RequestMethod.POST)
    public ResultMapUtil getJKBuildingList() {
        return buildingService.getJKBuildingList();
    }

    /**
     * 将金科楼栋映射成安居宝楼栋
     * @return
     */
    @CrossOrigin("*")
    @PostMapping(value = "/mappedAJBbuilding")
    public ResultMapUtil mappedAJBBuilding() {
        return buildingService.mappedAJBBuilding();
    }

    /**
     * 根据金科小区id查询金科楼栋数据
     * @param projectId
     * @return
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/getJKbuildingByProjectId", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> selectJKBuildingByProjectId (String projectId) {
        JsonResult jsonResult = new JsonResult();
        try {
            List<JKBuildingBase> jkBuildingBases = buildingService.getJKBuildingByProjectId(projectId);
            if (!jkBuildingBases.isEmpty()) {
                jsonResult.setStatus("success");
                jsonResult.setMsg("查询金科楼栋数据成功!");
                jsonResult.setResult(jkBuildingBases);
            }else {
                jsonResult.setStatus("success");
                jsonResult.setMsg("该小区下无基础数据!");
            }
        }catch (Exception e) {
            jsonResult.setResult(e.getClass().getName() + ":" + e.getMessage());
            jsonResult.setMsg("请求错误!");
            jsonResult.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * 根据projectId查询金科楼栋Id和Name列表
     * @param projectId 金科小区id
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/getjkbuildIdAndNamelist")
    public ResponseEntity<JsonResult> selectJKBuildIdAndNameList(String projectId) {
        JsonResult jsonResult = new JsonResult();
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            List<JKBuildingBase> jkBuildingBaseList = buildingService.getJKBuildingByProjectId(projectId);
            if (!jkBuildingBaseList.isEmpty()) {
                //利用迭代器Iterator遍历
                for (Iterator iterators = jkBuildingBaseList.iterator(); iterators.hasNext(); ) {
                    Map<String, String> map = new HashMap<>();
                    //获取当前遍历的元素，指定为JKBuildingBase对象
                    JKBuildingBase jkBuildingBase = (JKBuildingBase) iterators.next();
                    map.put("build_id", jkBuildingBase.getBuildId());
                    map.put("build_name", jkBuildingBase.getBuildName());
                    mapList.add(map);
                }
                jsonResult.setStatus("success");
                jsonResult.setMsg("查询金科楼栋Id和Name列表成功!");
                jsonResult.setResult(mapList);
            }else {
                jsonResult.setStatus("success");
                jsonResult.setMsg("该小区下无基础数据!");
            }
        } catch (Exception e) {
            jsonResult.setResult(e.getClass().getName() + ":" + e.getMessage());
            jsonResult.setMsg("请求错误!");
            jsonResult.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * 根据projectId和buildId查询金科楼栋数据
     * @param projectId
     * @param buildId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectJKbuildlistByJKbuildId")
    public ResponseEntity<JsonResult> selectJKbuildByJKbuildId(String projectId, String buildId) {
        JsonResult jsonResult =new JsonResult();
        try {
            List<JKBuildingBase> jkBuildingBases = buildingService.getJKBuildByBuildId(projectId, buildId);
            if (!jkBuildingBases.isEmpty()) {
                jsonResult.setResult(jkBuildingBases);
                jsonResult.setStatus("success");
                jsonResult.setMsg("查询记录成功!");
            }else {
                jsonResult.setMsg("查询结果为空");
            }
        }catch (Exception e) {
            jsonResult.setResult(e.getClass().getName() + ":" + e.getMessage());
            jsonResult.setStatus("error");
            jsonResult.setMsg("请求错误!");
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * 根据projectId查询安居宝楼栋数据
     * @param projectId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/getAJBbuildingByProjectId")
    public ResponseEntity<JsonResult> selectAJBBuildByProjectId(String projectId){
        JsonResult result = new JsonResult();
        try {
            List<AJBBuilding> ajbBuildingList = buildingService.getAJBBuildByProjectId(projectId);
            if (!ajbBuildingList.isEmpty()){
                result.setMsg("查询安居宝楼栋数据成功!");
                result.setStatus("success");
                result.setResult(ajbBuildingList);
            }else {
                result.setMsg("查询结果为空,该小区下无基础数据!");
                result.setStatus("success");
            }
        }catch (Exception e) {
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据金科楼栋id 查询安居宝楼栋
     * @param jkBuildId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "selectAJBbuildlistByJKbuildId")
    public ResponseEntity<JsonResult> selectAJBBuildByJKBuildId(String jkBuildId){
        JsonResult result = new JsonResult();
        try {
            List<AJBBuilding> ajbBuildings = buildingService.getAJBBuildByJKBuildId(jkBuildId);
            if (!ajbBuildings.isEmpty()) {
                result.setMsg("查询安居宝楼栋成功!");
                result.setStatus("success");
                result.setResult(ajbBuildings);
            }else {
                result.setMsg("查询结果为空,该楼栋下无基础数据!");
                result.setStatus("success");
            }
        }catch (Exception e) {
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }
}
