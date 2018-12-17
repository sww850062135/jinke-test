package com.sww.jinke.test.controller;


import com.sww.jinke.test.entity.AJBCommunity;
import com.sww.jinke.test.entity.JsonResult;
import com.sww.jinke.test.service.CommunityService;
import com.sww.jinke.test.util.ResultMapUtil;
import com.sww.jinke.test.entity.JKCommunityBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 15:27 2018/11/13
 */
@RestController
@RequestMapping(value = "/api/base")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    /**
     * 拉取金科小区数据
     * @return Map
     * @throws Exception
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/getJKcommunitylist", method = RequestMethod.POST)
    public ResultMapUtil getJKCommunityList(String companyId) throws Exception {
        return communityService.getJKCommunityList(companyId);
    }

    /**
     * 将金科小区映射成安居宝小区
     * @return
     * @throws Exception
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/mappedAJBcommunitylist",method = RequestMethod.POST)
    public ResultMapUtil mappedAJBCommunityList() throws Exception {
        return communityService.mappedAJBCommunityList();
    }


    /**
     * 查询金科小区数据列表
     * @param pageNum  当前页
     * @param pageSize 分页页大小
     * @return json
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/selectJKcommunitylist", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> selectJKCommunityList (int pageNum, int pageSize){
        JsonResult jsonResult = new JsonResult();
        try {
            List<JKCommunityBase> jkCommunityBases = communityService.selectJKCommunityList(pageNum, pageSize);
            jsonResult.setStatus("success");
            jsonResult.setMsg("查询金科小区数据列表成功!");
            jsonResult.setResult(jkCommunityBases);
        }catch (Exception e) {
            jsonResult.setResult(e.getClass().getName() + ":" + e.getMessage());
            jsonResult.setMsg("请求错误!");
            jsonResult.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonResult);
    }

    /**
     * 查询金科小区 id，name列表
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/getjkcommunityIdAndNamelist")
    public ResponseEntity<JsonResult> selectJKCommunityIdAndNameList() {
        JsonResult jsonResult = new JsonResult();
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            List<JKCommunityBase> jkCommunityBaseList = communityService.selectJKCommunityList();
            if (!jkCommunityBaseList.isEmpty()) {
                //利用迭代器Iterator遍历
                for (Iterator iterators = jkCommunityBaseList.iterator(); iterators.hasNext(); ) {
                    Map<String, String> map = new HashMap<>();
                    //获取当前遍历的元素，指定为JKCommunityBase对象
                    JKCommunityBase jkCommunityBase = (JKCommunityBase) iterators.next();
                    map.put("project_id", jkCommunityBase.getProjectId());
                    map.put("project_name", jkCommunityBase.getProjectName());
                    mapList.add(map);
                }
                jsonResult.setStatus("success");
                jsonResult.setMsg("查询金科小区Id和Name列表成功!");
                jsonResult.setResult(mapList);
            }else {
                jsonResult.setStatus("该小区下无基础数据!");
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
     * 根据金科项目ID查询小区数据
     * @param projectId 金科项目id
     * @return json
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/getJKcommunityByProjectId", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> getJKCommunityByProjectId(String projectId){
        JsonResult jsonResult = new JsonResult();
        try {
            List<JKCommunityBase> jkCommunityBaseList = communityService.selectJKCommunityByProjectId(projectId);
            if (!jkCommunityBaseList.isEmpty()) {
                jsonResult.setResult(jkCommunityBaseList);
                jsonResult.setStatus("success");
                jsonResult.setMsg("查询记录成功!");
            }else {
                jsonResult.setStatus("success");
                jsonResult.setMsg("查询结果为空,该小区下无基础数据!");
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
     * 根据金科项目ID查询安居宝小区
     * @param projectId 金科项目id
     * @return json
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/getAJBcommunityByProjectId", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> getAJBCommunityByProjectId(String projectId){
        JsonResult result = new JsonResult();
        try {
            List<AJBCommunity> ajbCommunityList = communityService.selectAJBCommunityByProjectId(projectId);
            if (!ajbCommunityList.isEmpty()) {
                result.setResult(ajbCommunityList);
                result.setStatus("success");
                result.setMsg("查询安居宝小区记录成功!");
            }else {
                result.setMsg("查询结果为空");
            }
        }catch (Exception e) {
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setStatus("error");
            result.setMsg("请求错误!");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 查询安居宝小区数据列表
     * @param pageNum  当前页
     * @param pageSize 分页页大小
     * @return json
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/selectAJBcommunitylist", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> selectAJBCommunityList (int pageNum, int pageSize){
        JsonResult jsonResult = new JsonResult();
        try {
            List<AJBCommunity> ajbCommunityList = communityService.selectAJBCommunityList(pageNum, pageSize);
            jsonResult.setStatus("success");
            jsonResult.setMsg("查询安居宝小区数据列表成功!");
            jsonResult.setResult(ajbCommunityList);
        }catch (Exception e) {
            jsonResult.setResult(e.getClass().getName() + ":" + e.getMessage());
            jsonResult.setMsg("请求错误!");
            jsonResult.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonResult);
    }


    /**
     * 根据金科项目ID查询安居宝小区编码
     * @param projectId 金科项目id
     * @return json
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/selectAJBCommunityCode", method = RequestMethod.GET)
    public ResponseEntity<JsonResult> selectAJBCommunityCode(String projectId){
        JsonResult result = new JsonResult();
        try {
            String ajbCommunityCode = communityService.selectAJBCommunityCode(projectId);
            if (ajbCommunityCode == null) {
                result.setMsg("该小区尚未绑定安居宝小区!");
            }else {
                result.setResult(ajbCommunityCode);
                result.setStatus("success");
                result.setMsg("查询安居宝小区编码成功!");
            }
        }catch (Exception e) {
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setStatus("error");
            result.setMsg("请求错误!");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }
}
