package com.example.demo.Controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.Model.User;
import com.example.demo.Service.EmailSendService;
import com.example.demo.Service.UserService;
import com.example.demo.cache.GuavaCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private GuavaCache cache;

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
        String cachedValue = cache.getCachedString("verifyCode");
        logger.info("verifyCode:" + verifyCode + "cachedValue:" + cachedValue);

        if (StringUtils.equals(verifyCode, cachedValue)) {
            attributes.addFlashAttribute("msg", "验证码错误");
            return "login";
        }
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "index";
        } else {
            attributes.addFlashAttribute("msg", "用户名或密码错误");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
