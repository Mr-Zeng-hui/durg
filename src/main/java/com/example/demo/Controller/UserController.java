package com.example.demo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import com.example.demo.util.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user")
    public String user(){
        return "user";
    }

    @GetMapping("/addUser")
    public String addUser(){
        return "addUser";
    }

    /*@GetMapping("/userList")
    public String userList(){
        return "addUser";
    }*/


    @RequestMapping("/add")
    @ResponseBody
    public JSONObject userAdd(@RequestBody JSONObject jsonObject){
        JSONObject json = new JSONObject();
        json.put("msg", "新增用户失败");
        json.put("code", 400);
        try {
//            String userName = user.getUserName();
            String verifyCode = jsonObject.getString("verifyCode");
            String email_acc = jsonObject.getString("email_acc");
            User user = jsonObject.toJavaObject(User.class);
            user.setEmail(email_acc);
            String password = user.getPassword();
            String cachedValue = CacheUtil.get("CODE_" + email_acc).toString();
            logger.info("verifyCode:" + verifyCode + "cachedValue:" + cachedValue);

            if (!StringUtils.equals(verifyCode, cachedValue)) {
                logger.info("验证码错误");
                json.put("msg", "验证码错误");
                json.put("code", 400);
                return json;
            }
            User user1 = userService.checkUser(email_acc, password);
            if (user1 != null){
                logger.info("该邮箱已存在");
                json.put("msg", "该邮箱已存在");
                json.put("code", 400);
                return json;
            }
            user.setId(UUID.randomUUID().toString());
            int i = userService.insertUser(user);
            if (i >0){
                logger.info("新增用户成功");
                json.put("msg", "新增用户成功");
                json.put("code", 200);
                return json;
            }
            logger.info("新增用户失败");

        }catch (Exception e){
            logger.error(e.getMessage(), e);
            json.put("msg", e.getMessage());
            json.put("code", 400);
        }

        return json;
    }

    @RequestMapping("/delSimpleUser")
    @ResponseBody
    public JSONObject delSimpleUser(@RequestParam("id") String id){
        JSONObject json = new JSONObject();
        json.put("msg", "删除用户失败");
        json.put("code", 400);
        try {
            userService.deleteUser(id);
            json.put("msg", "删除用户成功");
            json.put("code", 200);
            return json;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            json.put("msg", e.getMessage());
            json.put("code", 400);
        }
        return json;
    }


    @RequestMapping("/deleteUser")
    @ResponseBody
    public JSONObject deleteUser(@RequestBody JSONObject jsonObject){
        JSONObject json = new JSONObject();
        json.put("msg", "删除用户失败");
        json.put("code", 400);
        try {
            String userIds = jsonObject.getString("userids");
            List<String> list = Arrays.asList(userIds.split(","));
            if (list.size() > 0){
                for (String id : list){
                    userService.deleteUser(id);
                }
            }
                json.put("msg", "删除用户成功");
                json.put("code", 200);
                return json;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            json.put("msg", e.getMessage());
            json.put("code", 400);
        }

        return json;
    }

    @GetMapping("/getUserList")
    @ResponseBody
    public JSONObject getUserList(@RequestParam Map jsonObject){
        JSONObject json = new JSONObject();
        json.put("msg", "查询失败");
        json.put("code", 400);
        try {
            List<User> users = userService.selectAll(jsonObject);
            if (users != null){
                json.put("msg", "查询用户成功");
                json.put("code", 0);
                json.put("data", users);
                return json;
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            json.put("msg", e.getMessage());
            json.put("code", 400);
        }

        return json;
    }



}
