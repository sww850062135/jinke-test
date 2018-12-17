package com.sww.jinke.test.entity;

import lombok.Getter;
import lombok.Setter;


/**
 * 安居宝小区实体
 */
@Setter
@Getter
public class AJBCommunity {
    private String id;                       //主键id
    private String communityCode;           //小区编码
    private String communityName;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String createdTime;
    private String updateTime;

    @Override
    public String toString() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("AJBCommunity{id:").append(this.id).append(",");
        stringBuffer.append("community_code:").append(this.communityCode).append(",");
        stringBuffer.append("community_name:").append(this.communityName).append(",");
        stringBuffer.append("province_name:").append(this.provinceName).append(",");
        stringBuffer.append("city_name:").append(this.cityName).append(",");
        stringBuffer.append("district_name:").append(this.districtName).append(",");
        stringBuffer.append("created_time:").append(this.createdTime).append(",");
        stringBuffer.append("update_time:").append(this.updateTime).append("}");
        return stringBuffer.toString();
    }
}


