package com.example.demo.Controller;

import com.example.demo.Service.IDrugSerivce;
import com.example.demo.Service.ILogSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class LogControlller {

    @Autowired
    private ILogSerivce iLogSerivce;

    @GetMapping("/loglist")
    public String durglist() {
        return "loglist";
    }


    @GetMapping("/logData")
    @ResponseBody
    public Map<String, Object> getDrugsByPage(@RequestParam("page") int page, @RequestParam("limit") int limit , @RequestParam(value= "bak", required = false) String bak) {
        return iLogSerivce.queryForPage(page, limit, bak);
    }
}
