package com.sww.jinke.test.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JKUnitBase {
    private String unitId;
    private String unitName;
    private String buildId;
    private String buildName;
    private String projectId;
    private String projectName;
    private String syncTime;


    @Override
    public String toString() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("JKUnit{project_id:").append(this.projectId).append(",");
        stringBuffer.append("project_name:").append(this.projectName).append(",");
        stringBuffer.append("build_id:").append(this.buildId).append(",");
        stringBuffer.append("build_name:").append(this.buildName).append(",");
        stringBuffer.append("unit_id:").append(this.unitId).append(",");
        stringBuffer.append("unit_name:").append(this.unitName).append(",");
        stringBuffer.append("sync_time:").append(this.syncTime).append("}");
        return stringBuffer.toString();
    }
}
