package com.example.demo.Controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.Model.User;
import com.example.demo.Service.EmailSendService;
import com.example.demo.Service.UserService;
import com.example.demo.util.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;


    private static Logger logger = LoggerFactory.getLogger(EmailSendService.class);


    @GetMapping()
    public String loginPage() {
        return "/login";
    }


    @PostMapping("/login")
    public String login(@RequestBody JSONObject jsonInfo,
                        HttpSession session,
                        RedirectAttributes attributes) {
        String username = jsonInfo.getString("username");
        String password = jsonInfo.getString("password");
        String verifyCode = jsonInfo.getString("verifyCode");

        if (verifyCount(username)){
            attributes.addFlashAttribute("msg", "登录错误超过3次,等待1分钟后重试");
            logger.info("登录错误超过3次,等待1分钟后重试");
            return "login";
        }
        if (StringUtils.isNotBlank(verifyCode)){
            String cachedValue = CacheUtil.get("CODE_"+username).toString();
            logger.info("verifyCode:" + verifyCode + "cachedValue:" + cachedValue);
            if (!StringUtils.equals(verifyCode, cachedValue)) {
                attributes.addFlashAttribute("msg", "验证码错误");
                addCachedValue(username);
                logger.info("验证码错误");
                return "login";
            }
            return "index";
        }else {
            User user = userService.checkUser(username, password);
            logger.info("user:" + user);
            if (user != null) {
                user.setPassword(null);
                session.setAttribute("user", user);
                return "index";
            }else {
                attributes.addFlashAttribute("msg", "账户密码错误");
                addCachedValue(username);
                logger.info("账户密码错误");
                return "login";
            }

        }


    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    public static String addCachedValue(String value) {
        Object cachedValue = CacheUtil.get("ERROR_COUNT_" + value);
        if (cachedValue != null) {
           int count= Integer.valueOf(cachedValue.toString())+1;
            CacheUtil.put("ERROR_COUNT_" + value,count,1);
        }else {
            CacheUtil.put("ERROR_COUNT_" + value,1,1);
        }
        return "";
    }
    public static boolean verifyCount(String value) {
        Object cachedValue = CacheUtil.get("ERROR_COUNT_" + value);
        if (cachedValue != null) {
            int count= Integer.valueOf(cachedValue.toString());
           if (count > 3){
               return true;
           }
        }
        return false;
    }

    public static void main(String[] args) {
        CacheUtil.put("errorCount", "1", 1);
        System.out.println(CacheUtil.get("errorCount"));
    }
}
