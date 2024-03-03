package com.example.demo.Service.imp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.example.demo.Mapper.DrugDao;
import com.example.demo.Mapper.LogDao;
import com.example.demo.Model.Drug;
import com.example.demo.Model.Log;
import com.example.demo.Model.User;
import com.example.demo.Service.IDrugSerivce;
import com.example.demo.Service.ILogSerivce;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogServiceImpl implements ILogSerivce {
    @Autowired
    private LogDao logDao;
    private static Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Override
    public Map<String, Object> queryForPage(int pageNum, int pageSize, String bak, User user) {
        logger.info("用户信息:"+user);
        //if(user == null){
        if(user == null){

            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", 0);
            result.put("data", "当前未登录");
            return result;
        }

        int offset = (pageNum - 1) * pageSize;
        if("2".equals(user.getId())){
            //说明是管理员
            List<Log> drugList = logDao.queryForPage(pageSize, offset, bak);
            int totalCount = logDao.queryTotalCount(bak);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalCount);
            result.put("data", drugList);
            return result;
        }else{
            List<Log> drugList = logDao.queryForPageByUserId(pageSize, offset, bak, user.getId());
            int totalCount = logDao.queryTotalCountByUserId(bak, user.getId());
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalCount);
            result.put("data", drugList);
            return result;
        }
    }

    @Override
    public int queryTotalCount() {
        return 0;
    }

    @Override
    public boolean insertLog(String durgid, String durgname, String userid, String username, String time, String type) {
        // 获取当前时间字符串，默认格式为 "yyyy-MM-dd HH:mm:ss"
        String currentDateTime = DateUtil.now();
        // 可以指定自定义的日期格式
        String customFormat = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
        return logDao.insertLog(String.valueOf(UUID.fastUUID()), durgid, durgname, userid, username, time, type);
    }

}
