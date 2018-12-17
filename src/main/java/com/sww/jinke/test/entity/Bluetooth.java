package com.sww.jinke.test.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 10:52 2018/12/14
 */
@Setter
@Getter
public class Bluetooth {
    //小区号
    private String communityCode;
    //楼栋房号
    private String roomCode;
    //结束时间
    private String endDate;
    //蓝牙开锁码
    private String bluetoothCode;
}
