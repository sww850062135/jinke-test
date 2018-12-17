package com.sww.jinke.test.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Sun Weiwen
 * @Description:  二维码、通行码实体
 * @Date: 14:07 2018/12/14
 */
@Setter
@Getter
public class OpenDoor {
    //小区号
    private String communityCode;
    //楼栋房号
    private String roomCode;
    //开门次数
    private int codeCount;
    //开始时间
    private String startDate;
    //结束时间
    private String endDate;
    //状态
    private String state;
    //开锁码
    private String doorCode;
}
