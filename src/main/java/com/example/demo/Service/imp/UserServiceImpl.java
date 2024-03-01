package com.example.demo.Service.imp;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import com.example.demo.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public  List<User>  selectAll(JSONObject jsonObject) {
        StringBuilder  builder = new StringBuilder();
        if (jsonObject.getString("email") != null){
            builder.append(" and email = "+jsonObject.getString("email"));
        }else if (jsonObject.getString("userName") != null){
        builder.append(" and userName = "+jsonObject.getString("userName"));
        } else if (jsonObject.getString("type")!= null) {
            builder.append(" and type = "+jsonObject.getString("type"));
        } else if (jsonObject.getString("state")!= null) {
            builder.append(" and state = "+jsonObject.getString("state"));
        } else if (jsonObject.getString("securityIssues")!= null) {
            builder.append(" and securityIssues like % "+jsonObject.getString("securityIssues")+"%");
        }
        List<User> users = userMapper.selectAll(builder.toString());
        return users;
    }


}
