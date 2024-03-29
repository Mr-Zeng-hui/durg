package com.example.demo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Model.Email;

import com.example.demo.Service.EmailSendService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/email")
public class EmailSendController {
    @Autowired
    EmailSendService emailSendService;

    private static Logger logger = LoggerFactory.getLogger(EmailSendService.class);
    @ResponseBody
    @RequestMapping("/send") //短信发送
    public JSONObject sendEmail(@RequestBody JSONObject jsonInfo, HttpServletRequest request){
        logger.info("发送邮件入参为=="+jsonInfo);
        JSONObject result = emailSendService.sendEmail(jsonInfo);
        return result;
    }

    @ResponseBody
    @RequestMapping("/updateEmail") //短信信息修改
    public JSONObject updateEmail(@RequestBody Email email, HttpServletRequest request){
        logger.info("短信信息修改入参为=="+email);
        JSONObject result = emailSendService.updateEmail(email);
        return result;
    }

    @ResponseBody
    @RequestMapping("/getEmailList") //短信信息修改
    public JSONObject getEmailList(HttpServletRequest request){
        JSONObject jsonObject =new JSONObject();
        try {
            Email email = emailSendService.selectAllEmail();
            jsonObject.put("msg","查询成功");
            jsonObject.put("data",email);
            jsonObject.put("code",200);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            jsonObject.put("msg","查询失败");
            jsonObject.put("code",400);
        }

        return jsonObject;
    }

}
