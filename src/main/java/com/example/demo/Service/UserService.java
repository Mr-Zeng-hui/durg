package com.example.demo.Service;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.Model.User;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface UserService {

    public User checkUser(String username, String password);
    public User checkUserEmail(String emailAccount);

    public int insertUser(User user);

    public int resetPassword(User user);

    public int deleteUser(String userName);

    public List<User> selectAll(JSONObject jsonObject);
}
