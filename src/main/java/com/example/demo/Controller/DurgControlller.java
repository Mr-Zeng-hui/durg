package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DurgControlller {

    @GetMapping("/durglist")
    public String durglist(){
        return "durglist";
    }



}
