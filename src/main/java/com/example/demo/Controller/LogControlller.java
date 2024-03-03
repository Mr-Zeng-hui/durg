package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Service.IDrugSerivce;
import com.example.demo.Service.ILogSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
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
    public Map<String, Object> getDrugsByPage(HttpSession session, @RequestParam("page") int page, @RequestParam("limit") int limit , @RequestParam(value= "bak", required = false) String bak, @RequestParam("userid") String userid) {
        //User user = (User) session.getAttribute("user");
        User user = new User();
        user.setId(userid);
        return iLogSerivce.queryForPage(page, limit, bak, user);
    }
}
