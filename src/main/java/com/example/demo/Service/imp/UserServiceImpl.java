package com.example.demo.Service.imp;


import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import com.example.demo.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {
        User user = userMapper.queryByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }


}
