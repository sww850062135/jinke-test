package com.sww.jinke.test.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JKHouseBase {

    private String houseId;
    private String houseName;
    private String houseType;
    private String unitId;
    private String unitName;
    private String buildId;
    private String buildName;
    private String projectId;
    private String projectName;
    private String projectType;
    private String companyName;
    private String syncTime;

    @Override
    public String toString() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("JKHouse{company_name:").append(this.companyName).append(",");
        stringBuffer.append("project_id:").append(this.projectId).append(",");
        stringBuffer.append("project_name:").append(this.projectName).append(",");
        stringBuffer.append("project_type:").append(this.projectType).append(",");
        stringBuffer.append("build_id:").append(this.buildId).append(",");
        stringBuffer.append("build_name:").append(this.buildName).append(",");
        stringBuffer.append("unit_id:").append(this.unitId).append(",");
        stringBuffer.append("unit_name:").append(this.unitName).append(",");
        stringBuffer.append("house_id:").append(this.houseId).append(",");
        stringBuffer.append("house_name:").append(this.houseName).append(",");
        stringBuffer.append("house_type:").append(this.houseType).append(",");
        stringBuffer.append("sync_time:").append(this.syncTime).append("}");

        return stringBuffer.toString();
    }

}
