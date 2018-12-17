package com.sww.jinke.test.entity;


import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 14:06 2018/11/13
 */
@Getter
@Setter
public class JKCompanyBase {
    private String id;
    private String name;
    private String provinceId;
    private String provinceName;


    @Override
    public String toString() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("Company{company_id:").append(this.id).append(",");
        stringBuffer.append("company_name:").append(this.name).append(",");
        stringBuffer.append("province_id:").append(this.provinceId).append(",");
        stringBuffer.append("province_name:").append(this.provinceName).append("}");
        return stringBuffer.toString();
    }

}
