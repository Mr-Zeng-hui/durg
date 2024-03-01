package com.example.demo.Service.imp;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import com.example.demo.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {
        User user = userMapper.queryByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    public User checkUserEmail(String emailAccount) {
        User user = userMapper.checkUserEmail(emailAccount);
        return user;
    }

    public int insertUser(User user) {
        int insert = userMapper.insert(user);
        return insert;
    }

    public int resetPassword(User user) {
        int i = userMapper.resetPassword(user);
        return i;
    }

    public int deleteUser(String id) {
        int deleteUser = userMapper.deleteUser(id);
        return deleteUser;
    }

    public  List<User>  selectAll(Map jsonObject) {
        StringBuilder  builder = new StringBuilder();
        if (jsonObject.get("email") != null){
            builder.append(" and email = "+jsonObject.get("email"));
        }else if (jsonObject.get("userName") != null){
        builder.append(" and userName = "+jsonObject.get("userName"));
        } else if (jsonObject.get("type")!= null) {
            builder.append(" and type = "+jsonObject.get("type"));
        } else if (jsonObject.get("state")!= null) {
            builder.append(" and state = "+jsonObject.get("state"));
        } else if (jsonObject.get("securityIssues")!= null) {
            builder.append(" and securityIssues like % "+jsonObject.get("securityIssues")+"%");
        }
        List<User> users ;
        if (builder.length() > 0){
            users = userMapper.select(builder.toString());
        }else {
            users = userMapper.selectAll("");
        }
        return users;
    }


}
