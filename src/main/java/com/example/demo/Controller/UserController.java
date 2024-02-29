package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user")
    public String user(){
        return "user";
    }

    @GetMapping("/addUser")
    public String addUser(){
        return "addUser";
    }

}
