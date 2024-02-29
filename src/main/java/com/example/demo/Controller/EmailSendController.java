package com.example.demo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Model.ReqBody;
import com.example.demo.Service.EmailSendService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

}
