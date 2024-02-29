package com.example.demo.Service.imp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.example.demo.Mapper.DrugDao;
import com.example.demo.Model.Drug;
import com.example.demo.Service.IDrugSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DrugServiceImpl implements IDrugSerivce {
    @Autowired
    private DrugDao drugDao;

    @Override
    public Map<String, Object> queryForPage(int pageNum, int pageSize, String bak) {
        int offset = (pageNum - 1) * pageSize;
        List<Drug> drugList = drugDao.queryForPage(pageSize, offset, bak);
        int totalCount = drugDao.queryTotalCount();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", totalCount);
        result.put("data", drugList);
        return result;
    }

    @Override
    public int queryTotalCount() {
        return drugDao.queryTotalCount();
    }

    @Override
    public Boolean crawlingData(String id, String keyword) {
       try {
           // Python 脚本的路径
           String pythonScriptPath = "C://Users//hammer//IdeaProjects//durg//src//main//resources//python//baiduDrug.py";

           // 构造执行命令的字符串
           String command = "python " + pythonScriptPath+" "+keyword+" "+id;
           System.out.println(command);

           // 执行命令
           Process process = Runtime.getRuntime().exec(command);

           // 获取命令执行的输出流
           BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
           String line;
           while ((line = reader.readLine()) != null) {
               System.out.println(line);
           }

           // 等待命令执行完成
           int exitCode = process.waitFor();
           // 输出命令执行的结果码
           System.out.println("Exit Code: " + exitCode);

           return true;
       }catch (Exception e) {
           return false;
       }
    }

    @Override
    public boolean insertDrug(String name) {
// 获取当前时间字符串，默认格式为 "yyyy-MM-dd HH:mm:ss"
        String currentDateTime = DateUtil.now();
        System.out.println("Current Date and Time: " + currentDateTime);

        // 可以指定自定义的日期格式
        String customFormat = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
        return drugDao.insertDrugName(String.valueOf(UUID.fastUUID()), name, customFormat);
    }

    @Override
    public boolean delDrug(String id) {
        return drugDao.delDrugName(id);
    }

//    public static void main(String[] args) {
//        try {
//            // Python 脚本的路径
//            String pythonScriptPath = "C://Users//hammer//IdeaProjects//durg//src//main//resources//python//baiduDrug.py";
//
//            // 构造执行命令的字符串
//            String command = "python " + pythonScriptPath+" 布洛芬胶囊";
//            System.out.println(command);
//
//            // 执行命令
//            Process process = Runtime.getRuntime().exec(command);
//
//            // 获取命令执行的输出流
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            // 等待命令执行完成
//            int exitCode = process.waitFor();
//
//            // 输出命令执行的结果码
//            System.out.println("Exit Code: " + exitCode);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


}
