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
@RequestMapping("/verify")
public class VerifyController {



    private static Logger logger = LoggerFactory.getLogger(EmailSendService.class);
    @ResponseBody
    @RequestMapping("/verifyCode") //验证码校验
    public JSONObject verifyCode(@RequestBody JSONObject jsonInfo, HttpServletRequest request){
        JSONObject jsonObject=new JSONObject();
        String cachedValue = CacheUtil.get("verifyCode").toString();
        String code = jsonInfo.getString("code");
        logger.info("code:"+code+"cachedValue:"+cachedValue);
        if (StringUtils.equals(code, cachedValue)){
            jsonObject.put("code", 200);
            jsonObject.put("msg", "验证码正确");
        }else {
            jsonObject.put("code", 400);
            jsonObject.put("msg", "验证码错误");
        }
        return jsonObject;
    }

}
