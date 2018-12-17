package com.sww.jinke.test.controller;



import com.sww.jinke.test.entity.AJBHouse;
import com.sww.jinke.test.entity.JKHouseBase;
import com.sww.jinke.test.entity.JsonResult;
import com.sww.jinke.test.service.HouseService;
import com.sww.jinke.test.util.ResultMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 14:01 2018/11/15
 */
@RestController
@RequestMapping(value = "/api/base")
public class HouseController {
    @Autowired
    private HouseService houseService;

    /**
     * 同步金科房屋数据
     * @return Map
     */
    @CrossOrigin("*")
    @RequestMapping(value = "/getJKhouselist", method = RequestMethod.POST)
    public ResultMapUtil getJKHouseList(){
        return houseService.getJKHouseList();
    }

    /**
     * 将金科房屋数据映射成安居宝房屋数据
     * @return Map
     */
    @CrossOrigin("*")
    @PostMapping(value = "/mappedAJBhouse")
    public ResultMapUtil mappedAJBHouse() {
        return houseService.mappedAJBHouse();
    }

    /**
     * 根据buildId和unitId查询金科房屋Id和Name列表
     * @param buildId
     * @param unitId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectJKHouseIdAndNamelist")
    public ResponseEntity<JsonResult> selectJKHouseIdAndNameList(String buildId, String unitId){
        JsonResult result = new JsonResult();
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            List<JKHouseBase> jkHouseBaseList = houseService.getJKHouseListByBuildIdAndUnitId(buildId, unitId);
            if (!jkHouseBaseList.isEmpty()) {
                for (Iterator iterator = jkHouseBaseList.iterator(); iterator.hasNext(); ) {
                    Map<String, String> map = new HashMap<>();
                    JKHouseBase jkHouseBase = (JKHouseBase) iterator.next();
                    map.put("house_id", jkHouseBase.getHouseId());
                    map.put("house_name", jkHouseBase.getHouseName());
                    mapList.add(map);
                }

                result.setStatus("success");
                result.setMsg("查询金科房屋Id和Name列表成功!");
                result.setResult(mapList);

            }else {
                result.setStatus("success");
                result.setMsg("无该单元下无数据!");
            }
        }catch (Exception e){
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据buildId和unitId查询金科房屋列表
     * @param buildId
     * @param unitId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectJKHouselist")
    public ResponseEntity<JsonResult> selectJKHouseList(String buildId, String unitId){
        JsonResult result = new JsonResult();
        try {
            List<JKHouseBase> jkHouseBaseList = houseService.getJKHouseListByBuildIdAndUnitId(buildId, unitId);
            if (!jkHouseBaseList.isEmpty()) {
                result.setStatus("success");
                result.setMsg("查询金科房屋列表成功!");
                result.setResult(jkHouseBaseList);
            }else {
                result.setStatus("success");
                result.setMsg("查询结果为空!");
            }
        }catch (Exception e){
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据houseId查询金科房屋
     * @param houseId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectJKHouseByHouseId")
    public ResponseEntity<JsonResult> selectJKHouseByHouseId(String houseId){
        JsonResult result = new JsonResult();
        try {
            List<JKHouseBase> jkHouseBase = houseService.getJKHouseByHouseId(houseId);
            if (!jkHouseBase.isEmpty()) {
                result.setStatus("success");
                result.setMsg("查询金科房屋成功!");
                result.setResult(jkHouseBase);
            }else {
                result.setStatus("success");
                result.setMsg("查询结果为空!");
            }
        }catch (Exception e){
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据jkUnitId查询映射的安居宝房屋列表
     * @param jkUnitId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectAJBHouselistByJKUnitId")
    public ResponseEntity<JsonResult> selectAJBHouseListByJKUnitId(String jkUnitId){
        JsonResult result = new JsonResult();
        try {
            List<AJBHouse> ajbHouseList = houseService.selectAJBHouseListByJKUnitId(jkUnitId);
            if (!ajbHouseList.isEmpty()) {
                result.setStatus("success");
                result.setMsg("查询映射的安居宝房屋列表成功!");
                result.setResult(ajbHouseList);
            }else {
                result.setStatus("success");
                result.setMsg("查询结果为空,该单元无映射房屋数据,请先映射房屋数据!");
            }
        }catch (Exception e){
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据jkHouseId查询映射的安居宝房屋数据
     * @param jkHouseId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectAJBHouseByJKHouseId")
    public ResponseEntity<JsonResult> selectAJBHouseByJKHouseId(String jkHouseId){
        JsonResult result = new JsonResult();
        try {
            List<AJBHouse> ajbHouses = houseService.getAJBHouseByJKHouseId(jkHouseId);
            if (!ajbHouses.isEmpty()) {
                result.setStatus("success");
                result.setMsg("查询安居宝房屋成功!");
                result.setResult(ajbHouses);
            }else {
                result.setStatus("success");
                result.setMsg("查询结果为空,该房屋无映射数据!");
            }
        }catch (Exception e){
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据jkHouseId查询安居宝房屋编码
     * @param jkHouseId
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/selectAJBHouseCode")
    public ResponseEntity<JsonResult> selectAJBHouseCode(String jkHouseId){
        JsonResult result = new JsonResult();
        try {
            String ajbHouseCode = houseService.selectAJBHouseCode(jkHouseId);
            if (ajbHouseCode == null) {
                result.setMsg("该房屋尚未绑定安居宝房屋!");
            }else {
                result.setStatus("success");
                result.setMsg("查询安居宝房屋编码成功!");
                result.setResult(ajbHouseCode);
            }
        }catch (Exception e){
            result.setResult(e.getClass().getName() + ":" + e.getMessage());
            result.setMsg("请求错误!");
            result.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }
}
