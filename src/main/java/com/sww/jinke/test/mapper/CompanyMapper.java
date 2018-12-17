package com.sww.jinke.test.mapper;

import com.sww.jinke.test.entity.JKCompanyBase;
import org.apache.ibatis.annotations.Insert;

public interface CompanyMapper {
    @Insert("INSERT INTO jk_company(id, name, province_id, province_name) " +
            "VALUES(#{id}, #{name}, #{provinceId}, #{provinceName})")
    void add(JKCompanyBase JKCompanyBase);

}
