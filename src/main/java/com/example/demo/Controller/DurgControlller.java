package com.example.demo.Controller;

import com.example.demo.Model.Drug;
import com.example.demo.Service.EmailSendService;
import com.example.demo.Service.IDrugSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DurgControlller {
    private static Logger logger = LoggerFactory.getLogger(DurgControlller.class);

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
        logger.info("药品查询关键字:"+bak);
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
        logger.info("药品详情："+drug.toString());
        return drug.getInstructions();
    }

    @GetMapping("/durglistView")
    public String durglistView() {
        return "durglistView";
    }

    @GetMapping("/crawlingAllData")
    @ResponseBody
    public Boolean crawlingAllData() {
        return iDrugSerivce.crawlingAllData();
    }

}
