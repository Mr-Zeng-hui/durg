package com.example.demo.Controller;

import com.example.demo.Model.Drug;
import com.example.demo.Service.IDrugSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DurgControlller {

    @Autowired
    private IDrugSerivce iDrugSerivce;

    @GetMapping("/durglist")
    public String durglist() {
        return "durglist";
    }

    @GetMapping("/durgAdd")
    public String durgAdd() {
        return "durg_add";
    }

    @GetMapping("/durgData")
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


    @GetMapping("/durgDataAdd")
    @ResponseBody
    public String drugDataAdd(@RequestParam("name") String name) {
        iDrugSerivce.insertDrug(name);
        return "ok";
    }


    @GetMapping("/delDurg")
    @ResponseBody
    public String delDrug(@RequestParam("id") String id, @RequestParam("name") String name) {
        iDrugSerivce.delDrug(id, name);
        return "ok";
    }

    @GetMapping("/getDurgById")
    @ResponseBody
    public String getDurgById(@RequestParam("id") String id) {
        Drug drug = iDrugSerivce.getDurgById(id);
        System.out.println(drug.toString());
        return drug.getInstructions();
    }
}
