package com.sww.jinke.test.service;

import com.sww.jinke.test.entity.OpenDoor; /**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 14:30 2018/12/14
 */
public interface OpenDoorService {
    public OpenDoor generateDoorCode(OpenDoor openDoor) throws Exception;
}
