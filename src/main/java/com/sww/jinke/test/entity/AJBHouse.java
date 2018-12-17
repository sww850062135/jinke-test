package com.sww.jinke.test.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 14:04 2018/11/16
 */
@Setter
@Getter
public class AJBHouse {
    private String id;
    private String communityId;
    private String buildingId;
    private String unitId;
    private String houseCode;          //房屋编码
    private String houseName;
    private int floor;                 //楼层
    private String createdTime;
    private String updateTime;
}
