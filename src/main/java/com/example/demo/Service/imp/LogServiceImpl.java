package com.example.demo.Service.imp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.example.demo.Mapper.DrugDao;
import com.example.demo.Mapper.LogDao;
import com.example.demo.Model.Drug;
import com.example.demo.Model.Log;
import com.example.demo.Service.IDrugSerivce;
import com.example.demo.Service.ILogSerivce;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    public Map<String, Object> queryForPage(int pageNum, int pageSize, String bak) {
        int offset = (pageNum - 1) * pageSize;
        List<Log> drugList = logDao.queryForPage(pageSize, offset, bak);
        int totalCount = logDao.queryTotalCount(bak);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", totalCount);
        result.put("data", drugList);
        return result;
    }

    @Override
    public int queryTotalCount() {
        return 0;
    }

    @Override
    public boolean insertLog(String durgid, String durgname, String userid, String username, String time, String type) {
        // 获取当前时间字符串，默认格式为 "yyyy-MM-dd HH:mm:ss"
        String currentDateTime = DateUtil.now();
        System.out.println("Current Date and Time: " + currentDateTime);
        // 可以指定自定义的日期格式
        String customFormat = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
        return logDao.insertLog(String.valueOf(UUID.fastUUID()), durgid, durgname, userid, username, time, type);
    }

}
