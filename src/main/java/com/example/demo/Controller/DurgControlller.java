package com.example.demo.Controller;

import com.example.demo.Service.IDrugSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/drugInfo")
public class DurgControlller {

    @Autowired
    private IDrugSerivce iDrugSerivce;

    @GetMapping("/durglist")
    public String durglist() {
        return "durglist";
    }

    @GetMapping("/data")
    @ResponseBody
    public Map<String, Object> getDrugsByPage(@RequestParam("page") int page, @RequestParam("limit") int limit , @RequestParam(value= "bak", required = false) String bak) {
        System.out.println("bak:"+bak);
        return iDrugSerivce.queryForPage(page, limit, bak);
    }

    @GetMapping("/crawlingData")
    @ResponseBody
    public Boolean crawlingData(@RequestParam("id") String id, @RequestParam("keyword") String keyword) {
        return  iDrugSerivce.crawlingData(id, keyword);
    }



}
