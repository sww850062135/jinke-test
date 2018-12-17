package com.sww.jinke.test.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 16:35 2018/11/12
 */
@Setter
@Getter
public class JKCommunityBase {
    private String projectId;          //项目id
    private String projectName;        //项目名称
    private String provinceName;       //省份名称
    private String cityName;           //城市名称
    private String districtName;       //地区名称
    private String companyId;          //公司id
    private String companyName;        //公司名称
    private String houseTotal;         //房屋总数
    private String houseNum;           //房屋号
    private String syncTime;           //同步时间
    private String syncStatus;         //同步状态
    private String syncMsg;            //同步信息


    @Override
    public String toString() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("JKCommunity{project_id:").append(this.projectId).append(",");
        stringBuffer.append("project_name:").append(this.projectName).append(",");
        stringBuffer.append("province_name:").append(this.provinceName).append(",");
        stringBuffer.append("city_name:").append(this.cityName).append(",");
        stringBuffer.append("district_name:").append(this.districtName).append(",");
        stringBuffer.append("company_id:").append(this.companyId).append(",");
        stringBuffer.append("company_name:").append(this.companyName).append(",");
        stringBuffer.append("house_total:").append(this.houseTotal).append(",");
        stringBuffer.append("house_num:").append(this.houseNum).append(",");
        stringBuffer.append("sync_time:").append(this.syncTime).append(",");
        stringBuffer.append("sync_status:").append(this.syncStatus).append(",");
        stringBuffer.append("sync_msg:").append(this.syncMsg).append("}");
        return stringBuffer.toString();
    }
}
