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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;


    private static Logger logger = LoggerFactory.getLogger(LoginController.class);


    @GetMapping()
    public String loginPage() {
        return "login";
    }


    @ResponseBody
    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject jsonInfo,
                            HttpSession session,
                            RedirectAttributes attributes, HttpServletRequest request) {
        String username = jsonInfo.getString("username");
        String password = jsonInfo.getString("password");
        String verifyCode = jsonInfo.getString("verifyCode");
        JSONObject json = new JSONObject();
        try {
            if (verifyCount(username)) {

                logger.info("登录错误超过3次,等待1分钟后重试");
                json.put("msg", "登录错误超过3次,等待1分钟后重试");
                json.put("code", 400);
                return json;
            }
            if (StringUtils.isNotBlank(verifyCode)) {
                String cachedValue = CacheUtil.get("CODE_" + username).toString();
                logger.info("verifyCode:" + verifyCode + "cachedValue:" + cachedValue);

                if (!StringUtils.equals(verifyCode, cachedValue)) {

                    addCachedValue(username);
                    logger.info("验证码错误");
                    json.put("msg", "验证码错误");
                    json.put("code", 400);
                    return json;
                }
                User user = userService.checkUserEmail(username);

                if (user == null) {
                    user.setUserName(username);
                    user.setEmail(username);
                    user.setPassword("e10adc3949ba59abbe56e057f20f883e");
                    user.setId(UUID.randomUUID().toString());
                    user.setType("0");
                    user.setState("1");
                    int i = userService.insertUser(user);
                    logger.info("insertUser:" + i);
                }
                String token = user.getUserName() + "," + user.getPassword();
                request.setAttribute("token", token);
                CacheUtil.put(token, user, 120);
                user.setPassword(null);
                session.setAttribute("user", user);
                json.put("msg", "登录成功");
                json.put("code", 200);
                return json;
            } else {
                User user = userService.checkUser(username, password);
                logger.info("user:" + user);
                if (user != null) {
                    String token = user.getUserName() + "," + user.getPassword();
                    request.setAttribute("token", token);
                    CacheUtil.put(token, user, 120);
                    user.setPassword(null);
                    session.setAttribute("user", user);
                    json.put("msg", "登录成功");
                    json.put("code", 200);
                    return json;
                } else {

                    addCachedValue(username);
                    logger.info("账户密码错误");
                    json.put("msg", "账户密码错误");
                    json.put("code", 400);
                    return json;

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return json;
    }

    @ResponseBody
    @GetMapping("/logout")
    public JSONObject logout(HttpSession session, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            Object token = request.getAttribute("token");
            CacheUtil.remove(token.toString());
            session.removeAttribute("user");
            json.put("msg", "退出成功");
            json.put("code", 200);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            json.put("msg", "退出失败");
            json.put("code", 400);
        }

        return json;
    }

    public static String addCachedValue(String value) {
        Object cachedValue = CacheUtil.get("ERROR_COUNT_" + value);
        if (cachedValue != null) {
            int count = Integer.valueOf(cachedValue.toString()) + 1;
            CacheUtil.put("ERROR_COUNT_" + value, count, 1);
        } else {
            CacheUtil.put("ERROR_COUNT_" + value, 1, 1);
        }
        return "";
    }

    public static boolean verifyCount(String value) {
        Object cachedValue = CacheUtil.get("ERROR_COUNT_" + value);
        if (cachedValue != null) {
            int count = Integer.valueOf(cachedValue.toString());
            if (count > 3) {
                return true;
            }
        }
        return false;
    }


    @ResponseBody
    @RequestMapping("/registerUser")
    public JSONObject registerUser(@RequestBody User user, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        json.put("msg", "注册失败");
        json.put("code", 400);
        try {
            user.setId(UUID.randomUUID().toString());
            int i = userService.insertUser(user);
            if (i > 0) {
                json.put("msg", "注册成功");
                json.put("code", 200);
                return json;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            json.put("msg", e.getMessage());
            json.put("code", 400);
        }

        return json;
    }

    @ResponseBody
    @RequestMapping("/reset")
    public JSONObject reset(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        json.put("msg", "重置失败");
        json.put("code", 400);
        try {
            String username = jsonObject.getString("username");
            String oldPassword = jsonObject.getString("oldPassword");
            String newPassword = jsonObject.getString("newPassword");
            String verifyCode = jsonObject.getString("verifyCode");
            String cachedValue = CacheUtil.get("CODE_" + username).toString();
            if (!StringUtils.equals(verifyCode, cachedValue)) {
                logger.info("验证码错误");
                json.put("msg", "验证码错误");
                json.put("code", 400);
                return json;
            }
            User user = userService.checkUser(username, oldPassword);
            logger.info("user:" + user);
            if (user != null) {
                user.setPassword(newPassword);
                userService.resetPassword(user);
                json.put("msg", "重置成功");
                json.put("code", 200);
                return json;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            json.put("msg", e.getMessage());
            json.put("code", 400);
        }

        return json;
    }

    public static void main(String[] args) {
        CacheUtil.put("errorCount", "1", 1);
        System.out.println(CacheUtil.get("errorCount"));
    }
}
