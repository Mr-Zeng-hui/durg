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
    public Map<String, Object> getDrugsByPage(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return iDrugSerivce.queryForPage(page, limit);
    }

    @GetMapping("/crawlingData")
    @ResponseBody
    public Map<String, Object> crawlingData(@RequestParam("id") int id) {
        return null;
    }


}
