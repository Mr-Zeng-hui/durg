package com.example.demo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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


    @GetMapping("/add")
    @ResponseBody
    public JSONObject userAdd(@RequestBody User user){
        JSONObject json = new JSONObject();
        json.put("msg", "新增用户失败");
        json.put("code", 400);
        try {
            String userName = user.getUserName();
            String password = user.getPassword();
            String email = user.getEmail();
            User user1 = userService.checkUser(userName, password);
            if (user1 != null){
                logger.info("该用户已存在");
                json.put("msg", "该用户已存在");
                json.put("code", 400);
                return json;
            }
            String email1 = user1.getEmail();
            if (StringUtils.isNotBlank(email)&&StringUtils.equals(email1,email)){
                logger.info("该邮箱已存在");
                json.put("msg", "该邮箱已存在");
                json.put("code", 400);
                return json;
            }
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

    @GetMapping("/deleteUser")
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
                json.put("code", 200);
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
