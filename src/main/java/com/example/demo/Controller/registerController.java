package com.example.demo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Service.EmailSendService;
import com.example.demo.util.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/register")
public class registerController {

    private static Logger logger = LoggerFactory.getLogger(EmailSendService.class);
    @ResponseBody
    @RequestMapping("/registerUser")
    public JSONObject registerUser(@RequestBody JSONObject jsonInfo, HttpServletRequest request){
        JSONObject jsonObject=new JSONObject();



        return jsonObject;
    }
}
