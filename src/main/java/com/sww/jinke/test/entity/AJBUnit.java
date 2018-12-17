package com.sww.jinke.test.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 安居宝单元实体
 */
@Setter
@Getter
public class AJBUnit {
    private String id;
    private String communityId;
    private String buildingId;
    private String unitName;
    private String unitCode;
    private String createdTime;
    private String updateTime;

    @Override
    public String toString() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("AJBUnit{id:").append(this.id).append(",");
        stringBuffer.append("community_id:").append(this.communityId).append(",");
        stringBuffer.append("building_id:").append(this.buildingId).append(",");
        stringBuffer.append("unit_name:").append(this.unitName).append(",");
        stringBuffer.append("unit_code:").append(this.unitCode).append(",");
        stringBuffer.append("created_time:").append(this.createdTime).append(",");
        stringBuffer.append("update_time:").append(this.updateTime).append("}");
        return stringBuffer.toString();
    }
}
