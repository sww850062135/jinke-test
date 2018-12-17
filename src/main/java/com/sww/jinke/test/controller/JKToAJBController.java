package com.sww.jinke.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sww.jinke.test.entity.*;
import com.sww.jinke.test.service.JKToAJBService;
import com.sww.jinke.test.util.ResultMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: Sun Weiwen
 * @Description: 金科小区数据映射成安居宝小区数据
 * @Date: 15:17 2018/12/12
 */
@RestController
@RequestMapping("/api/ajb")
public class JKToAJBController {

    @Autowired
    private JKToAJBService jkToAJBService;

    /**
     * 将金科小区映射成安居宝小区
     * @return
     * @throws Exception
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/mappedAJBCommunity",method = RequestMethod.POST)
    public ResponseEntity<JsonResult> mappedAJBCommunityList(@RequestBody HashMap map) {
        JsonResult result = new JsonResult();
        try {
            System.out.println(map.toString());
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
            //System.out.println(jsonObject.toJSONString());
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("params");
            JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get("jkCommunityBaseList").toString());
            //System.out.println(jsonArray.toString());
            List<JKCommunityBase> jkCommunityBaseList;
            jkCommunityBaseList = JSONArray.parseArray(jsonArray.toJSONString(), JKCommunityBase.class);
            ResultMapUtil resultMapUtil = jkToAJBService.mappedAJBCommunity(jkCommunityBaseList);
            result.setStatus("success");
            result.setMsg1(resultMapUtil.get("message"));
            result.setResult(resultMapUtil.get("data"));
        }catch (Exception e){
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setStatus("error");
            result.setMsg("请求错误!");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 将金科楼栋映射成安居宝楼栋
     * @return
     */
    @CrossOrigin("*")
    @PostMapping(value = "/mappedAJBBuilding")
    public ResponseEntity<JsonResult> mappedAJBBuilding(@RequestBody HashMap map) {
        JsonResult result = new JsonResult();
        try {
            System.out.println(map.toString());
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
            //System.out.println(jsonObject.toJSONString());
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("params");
            JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get("jkBuildingBaseList").toString());
            String jkProjectId = (String) jsonObject1.get("projectId");
            //System.out.println(jsonArray.toString());
            List<JKBuildingBase> jkBuildingBaseList;
            jkBuildingBaseList = JSONArray.parseArray(jsonArray.toJSONString(), JKBuildingBase.class);
            ResultMapUtil resultMapUtil = jkToAJBService.mappedAJBBuilding(jkProjectId, jkBuildingBaseList);
            result.setStatus("success");
            result.setMsg1(resultMapUtil.get("message"));
            result.setResult(resultMapUtil.get("data"));
        }catch (Exception e) {
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setStatus("error");
            result.setMsg("请求错误!");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 将金科单元数据映射成安居宝单元数据
     * @return Map
     */
    @CrossOrigin("*")
    @PostMapping(value = "/mappedAJBUnit")
    public ResponseEntity<JsonResult> mappedAJBUnit(@RequestBody HashMap map) {
        JsonResult result = new JsonResult();
        try {
            System.out.println(map.toString());
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
            //System.out.println(jsonObject.toJSONString());
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("params");
            JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get("jkUnitBaseList").toString());
            String jkBuildId = (String) jsonObject1.get("buildId");
            //System.out.println(jsonArray.toString());
            List<JKUnitBase> jkUnitBaseList;
            jkUnitBaseList = JSONArray.parseArray(jsonArray.toJSONString(), JKUnitBase.class);
            ResultMapUtil resultMapUtil = jkToAJBService.mappedAJBUnit(jkBuildId, jkUnitBaseList);
            result.setStatus("success");
            result.setMsg1(resultMapUtil.get("message"));
            result.setResult(resultMapUtil.get("data"));
        }catch (Exception e) {
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setStatus("error");
            result.setMsg("请求错误!");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 将金科房屋数据映射成安居宝房屋数据
     * @return Map
     */
    @CrossOrigin("*")
    @PostMapping(value = "/mappedAJBHouse")
    public ResponseEntity<JsonResult> mappedAJBHouse(@RequestBody HashMap map) {
        JsonResult result = new JsonResult();
        try {
            System.out.println(map.toString());
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
            //System.out.println(jsonObject.toJSONString());
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("params");
            JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get("jkHouseBaseList").toString());
            String jkBuildId = (String) jsonObject1.get("buildId");
            //System.out.println(jsonArray.toString());
            List<JKHouseBase> jkHouseBaseList;
            jkHouseBaseList = JSONArray.parseArray(jsonArray.toJSONString(), JKHouseBase.class);
            ResultMapUtil resultMapUtil = jkToAJBService.mappedAJBHouse(jkBuildId, jkHouseBaseList);
            result.setStatus("success");
            result.setMsg1(resultMapUtil.get("message"));
            result.setResult(resultMapUtil.get("data"));
        }catch (Exception e) {
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setStatus("error");
            result.setMsg("请求错误!");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }
}
