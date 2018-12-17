package com.sww.jinke.test.controller;


import com.sww.jinke.test.service.CompanyService;
import com.sww.jinke.test.util.ResultMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: Sun Weiwen
 * @Description:
 * @Date: 14:14 2018/11/13
 */
@RestController
@RequestMapping(value = "/api/base")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @CrossOrigin("*")
    @RequestMapping(value = "/getcompanylist", method = RequestMethod.POST)
    public ResultMapUtil getCompanyList() {
        return companyService.getCompanyList();
    }

}
