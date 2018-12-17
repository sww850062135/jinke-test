package com.sww.jinke.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sww.jinke.test.entity.JsonResult;
import com.sww.jinke.test.entity.OpenDoor;
import com.sww.jinke.test.service.OpenDoorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 14:00 2018/12/14
 */
@RestController
@RequestMapping(value = "/api/code")
public class DoorCodeController {
    private static final Logger logger = LoggerFactory.getLogger(DoorCodeController.class);

    @Autowired
    private OpenDoorService openDoorService;

    @CrossOrigin("*")
    @PostMapping(value = "/generateDoorCode")
    public ResponseEntity<JsonResult> generateDoorCode(@RequestBody HashMap map) {
        JsonResult result = new JsonResult();
        OpenDoor openDoor = new OpenDoor();
        System.out.println(map.toString());
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        JSONObject jsonObject1 = (JSONObject) jsonObject.get("params");
        String community_code = (String) jsonObject1.get("community_code");
        String room_code = (String) jsonObject1.get("room_code");
        String start_date = (String) jsonObject1.get("start_date");
        String end_date = (String) jsonObject1.get("end_date");
        int code_count = (int) jsonObject1.get("code_count");
        //判断次数 如果次数为0 则默认为1
        if (0 == code_count) {
            openDoor.setCodeCount(1);
        }
        openDoor.setCommunityCode(community_code);
        openDoor.setRoomCode(room_code);
        openDoor.setStartDate(start_date);
        openDoor.setEndDate(end_date);
        openDoor.setCodeCount(code_count);
        try {
            //OpenDoor openDoor1 = openDoorService.generateDoorCode(openDoor);
            String doorCode = getRandomCode();
            logger.info("生成的通行码：", doorCode);
            result.setResult(doorCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * //生成6位随机通行证
     * @return
     */
    private String getRandomCode() {
        // 种子字符串
        String seed = "1234567890";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * seed.length());
            sb.append(seed.charAt(index));
        }
        return sb.toString();
    }
}
