package com.dcc.demo.controller;

import com.dcc.demo.model.User;
import com.dcc.demo.service.BaseCountInfoService;
import com.dcc.demo.service.UserService;
import com.dcc.demo.util.annotation.MyUserAnnotation;
import com.dcc.demo.vo.BaseCountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;


@RestController("/base")
public class BaseCountController {

    @Autowired
    private BaseCountInfoService baseCountInfoService;

    @RequestMapping("/syncBaseCountInfo")
    public Integer syncBaseCountInfo(){
        return baseCountInfoService.syncBaseCountInfo();
    }

    @RequestMapping("/getBaseCountInfo")
    public BaseCountInfo getBaseCountInfo(){
        return baseCountInfoService.getBaseCountInfo();
    }
}
