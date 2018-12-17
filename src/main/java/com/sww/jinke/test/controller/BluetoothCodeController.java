package com.sww.jinke.test.controller;

import cn.anjubao.api.BluetoothApi;
import cn.anjubao.bean.BluetoothBo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sww.jinke.test.entity.Bluetooth;
import com.sww.jinke.test.entity.JsonResult;
import com.sww.jinke.test.util.DateUtils;
import com.sww.jinke.test.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 10:21 2018/12/14
 */
@RestController
@RequestMapping(value = "/api/code")
public class BluetoothCodeController {
    private static final Logger logger = LoggerFactory.getLogger(BluetoothCodeController.class);
    @CrossOrigin("*")
    @PostMapping(value = "/generateBluetoothCode")
    public ResponseEntity<JsonResult> generateBluetoothCode(@RequestBody HashMap map) {
        JsonResult result = new JsonResult();
        Bluetooth bluetooth = new Bluetooth();
        System.out.println(map.toString());
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        JSONObject jsonObject1 = (JSONObject) jsonObject.get("params");
        try {
            String community_code = (String) jsonObject1.get("community_code");
            String room_code = (String) jsonObject1.get("room_code");
            String end_date = (String) jsonObject1.get("end_date");
            if (StringUtil.isNullOrEmpty(end_date)) {
                end_date = DateUtils.getNow_Pre_Date(Calendar.DATE, 7);
                bluetooth.setEndDate(end_date);
            }
            Date date = DateUtils.parse(end_date);
            BluetoothBo bluetoothBo = BluetoothBo.newBuilder()
                    .setCommunityCode(community_code)
                    .setRoomCode(room_code)
                    .setEndDate(date)
                    .build();
            String bluetoothCode = BluetoothApi.generateBluetoothCode(bluetoothBo);
            bluetooth.setBluetoothCode(bluetoothCode);
            result.setMsg("获取开锁码成功!");
            result.setResult( bluetoothCode);
            result.setStatus("success");
        }catch (Exception e) {
            logger.error("生成蓝牙开锁码出现错误: ",e);
            result.setMsg("生成蓝牙开锁码错误");
        }
        return ResponseEntity.ok(result);
    }
}
