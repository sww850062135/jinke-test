package com.sww.jinke.test.controller;

import com.sww.jinke.test.entity.AJBUnit;
import com.sww.jinke.test.entity.JKUnitBase;
import com.sww.jinke.test.entity.JsonResult;
import com.sww.jinke.test.service.UnitService;
import com.sww.jinke.test.util.ResultMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/api/base")
public class UnitController {

    @Autowired
    private UnitService unitService;

    /**
     * 同步金科单元数据
     * @return Map
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/getJKunitList", method = RequestMethod.POST)
    public ResultMapUtil getJKUnitList(){
        return unitService.getJKUnitList();
    }

    /**
     * 将金科单元数据映射成安居宝单元数据
     * @return Map
     */
    @CrossOrigin("*")
    @PostMapping(value = "/mappedAJBunit")
    public ResultMapUtil mappedAJBUnit() {
        return unitService.mappedAJBUnit();
    }

    /**
     * 根据buildId查询金科单元数据
     * @param buildId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectJKunitByBuildId")
    public ResponseEntity<JsonResult> selectJKUnitByBuildId(String buildId) {
        JsonResult result = new JsonResult();
        try {
            List<JKUnitBase> jkUnitBases = unitService.getJKUnitByBuildId(buildId);
            if (!jkUnitBases.isEmpty()) {
                result.setMsg("查询金科单元数据成功!");
                result.setStatus("success");
                result.setResult(jkUnitBases);
            }else {
                result.setStatus("success");
                result.setMsg("查询结果为空!");
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
     * 根据buildId查询金科单元Id和Name列表
     * @param buildId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectJKUnitIdAndNamelist")
    public ResponseEntity<JsonResult> selectJKUnitIdAndNameList(String buildId){
        JsonResult result = new JsonResult();
        List<Map<String, String>> mapList = new ArrayList<>();
        List<JKUnitBase> jkUnitBases = unitService.getJKUnitByBuildId(buildId);
        for (Iterator iterator = jkUnitBases.iterator(); iterator.hasNext();) {
            Map<String, String> map = new HashMap<>();
            JKUnitBase jkUnitBase = (JKUnitBase) iterator.next();
            map.put("unit_id", jkUnitBase.getUnitId());
            map.put("unit_name", jkUnitBase.getUnitName());
            mapList.add(map);
        }
        try {
            result.setMsg("查询金科单元Id和Name列表成功!");
            result.setStatus("success");
            result.setResult(mapList);
        }catch (Exception e) {
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }


    /**
     * 根据unitId查询金科单元数据
     * @param unitId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectJKUnitByUnitId")
    public ResponseEntity<JsonResult> selectJKUnitByUnitId(String unitId){
        JsonResult result = new JsonResult();
        try {
            List<JKUnitBase> jkUnitBases = unitService.getJKUnitByUnitId(unitId);
            if (!jkUnitBases.isEmpty()) {
                result.setMsg("查询金科单元数据成功!");
                result.setStatus("success");
                result.setResult(jkUnitBases);
            }else {
                result.setStatus("success");
                result.setMsg("查询结果为空!");
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
     * 根据jkUnitId查询映射的安居宝单元数据
     * @param jkUnitId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectAJBUnitByJKUnitId")
    public ResponseEntity<JsonResult> selectAJBUnitByJKUnitId(String jkUnitId){
        JsonResult result = new JsonResult();
        try {
            List<AJBUnit> ajbUnitList = unitService.getAJBUnitByJKUnitId(jkUnitId);
            if (!ajbUnitList.isEmpty()) {
                result.setMsg("查询安居宝单元数据成功!");
                result.setStatus("success");
                result.setResult(ajbUnitList);
            }else {
                result.setStatus("success");
                result.setMsg("查询结果为空,该单元无映射数据!");
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
     * 根据jkBuildId查询映射的安居宝单元数据
     * @param jkBuildId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectAJBunitlistByJKBuildId")
    public ResponseEntity<JsonResult> getAJBUnitListByJKBuildId(String jkBuildId) {
        JsonResult result = new JsonResult();
        try {
            List<AJBUnit> ajbUnitList = unitService.getAJBUnitListByJKBuildId(jkBuildId);
            if (!ajbUnitList.isEmpty()) {
                result.setMsg("查询金科单元列表数据成功!");
                result.setStatus("success");
                result.setResult(ajbUnitList);
            }else {
                result.setStatus("success");
                result.setMsg("查询结果为空,该楼栋下无映射数据!");
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
