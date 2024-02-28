package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginControlller {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

}
