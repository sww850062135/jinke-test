package com.sww.jinke.test.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 安居宝楼栋实体
 */
@Setter
@Getter
public class AJBBuilding {
    private String id;               //主键id
    private String communityId;      //小区id
    private String buildingCode;     //楼栋编码
    private String buildingName;
    private String createdTime;
    private String updateTime;


    @Override
    public String toString() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("AJBBuilding{id:").append(this.id).append(",");
        stringBuffer.append("community_id:").append(this.communityId).append(",");
        stringBuffer.append("building_code:").append(this.buildingCode).append(",");
        stringBuffer.append("building_name:").append(this.buildingName).append(",");
        stringBuffer.append("created_time:").append(this.createdTime).append(",");
        stringBuffer.append("update_time:").append(this.updateTime).append("}");
        return stringBuffer.toString();
    }
}
