package com.sww.jinke.test.entity;


import lombok.Getter;
import lombok.Setter;
/**
 * @Author: Sun Weiwen
 * @Description: 
 * @Date: 10:29 2018/11/12
 */

@Setter
@Getter
public class JKBuildingBase {

    private String buildId;
    private String buildName;
    private String projectId;
    private String projectName;
    private String syncTime;


    @Override
    public String toString() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("JKBuilding{project_id:").append(this.projectId).append(",");
        stringBuffer.append("project_name:").append(this.projectName).append(",");
        stringBuffer.append("build_id:").append(this.buildId).append(",");
        stringBuffer.append("build_name:").append(this.buildName).append(",");
        stringBuffer.append("sync_time:").append(this.syncTime).append("}");
        return stringBuffer.toString();
    }
}
