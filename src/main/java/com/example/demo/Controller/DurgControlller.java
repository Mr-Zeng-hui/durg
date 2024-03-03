package com.example.demo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Model.Drug;
import com.example.demo.Model.User;
import com.example.demo.Service.EmailSendService;
import com.example.demo.Service.IDrugSerivce;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public JSONObject crawlingData(@RequestParam("id") String id, @RequestParam("keyword") String keyword, HttpSession session, @RequestParam("userid") String userid) {
        JSONObject json = new JSONObject();
        json.put("msg", "爬取药品失败");
        json.put("code", 500);
        try {
            //User user = (User) session.getAttribute("user");
            User user = new User();
            user.setId(userid);
            //if(user ==null){
            if(StringUtils.isBlank(userid)){
                logger.warn("用户尚未登录");
                json.put("msg", "用户尚未登录");
                json.put("code", 500);
                return json;
            }
            iDrugSerivce.crawlingData(id, keyword, user);
            json.put("msg", "爬取药品成功");
            json.put("code", 200);
            return json;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return json;
        }
    }

    @GetMapping("/durgDataAdd")
    @ResponseBody
    public JSONObject drugDataAdd(@RequestParam("name") String name, HttpSession session, @RequestParam("userid") String userid) {
        JSONObject json = new JSONObject();
        json.put("msg", "添加药品失败");
        json.put("code", 500);
        try {
            //User user = (User) session.getAttribute("user");
            User user = new User();
            user.setId(userid);
            //if(user ==null){
            if(StringUtils.isBlank(userid)){
                logger.warn("用户尚未登录");
                json.put("msg", "用户尚未登录");
                json.put("code", 500);
                return json;
            }
            iDrugSerivce.insertDrug(name, user);
            json.put("msg", "添加药品成功");
            json.put("code", 200);
            return json;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return json;
        }
    }

    @GetMapping("/delDurg")
    @ResponseBody
    public JSONObject delDrug(@RequestParam("id") String id, @RequestParam("name") String name, HttpSession session, @RequestParam("userid") String userid) {
        JSONObject json = new JSONObject();
        json.put("msg", "删除药品失败");
        json.put("code", 500);
        try {
            //User user = (User) session.getAttribute("user");
            User user = new User();
            user.setId(userid);
            //if(user ==null){
            if(StringUtils.isBlank(userid)){
                logger.warn("用户尚未登录");
                json.put("msg", "用户尚未登录");
                json.put("code", 500);
                return json;
            }
            iDrugSerivce.delDrug(id, name ,user);
            json.put("msg", "删除药品成功");
            json.put("code", 200);
            return json;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return json;
        }
    }

    @GetMapping("/getDurgById")
    @ResponseBody
    public JSONObject getDurgById(@RequestParam("id") String id, HttpSession session, @RequestParam("userid") String userid) {
        JSONObject json = new JSONObject();
        json.put("msg", "查看说明书失败");
        json.put("code", 500);
        try {
            //User user = (User) session.getAttribute("user");
            User user = new User();
            user.setId(userid);
            //if(user ==null){
            if(StringUtils.isBlank(userid)){
                logger.warn("用户尚未登录");
                json.put("msg", "用户尚未登录");
                json.put("code", 500);
                return json;
            }
            Drug drug = iDrugSerivce.getDurgById(id, user);
            json.put("msg", "查看说明书成功");
            json.put("code", 200);
            json.put("data", drug.getInstructions());
            return json;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return json;
        }

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

    public static void main(String[] args) {
        try {
            String originalString = "小儿至宝丸";
            String encodedString = URLEncoder.encode(originalString, "UTF-8");
            System.out.println(encodedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
