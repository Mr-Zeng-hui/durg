package com.example.demo.Service.imp;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.example.demo.Controller.DurgControlller;
import com.example.demo.Mapper.DrugDao;
import com.example.demo.Mapper.LogDao;
import com.example.demo.Model.Drug;
import com.example.demo.Model.User;
import com.example.demo.Service.IDrugSerivce;
import com.example.demo.Service.InitDrugService;
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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DrugServiceImpl implements IDrugSerivce {

    @Autowired
    private DrugDao drugDao;

    @Autowired
    private LogDao logDao;
    private static Logger logger = LoggerFactory.getLogger(DrugServiceImpl.class);

    @Override
    public Map<String, Object> queryForPage(int pageNum, int pageSize, String bak) {
        int offset = (pageNum - 1) * pageSize;
        List<Drug> drugList = drugDao.queryForPage(pageSize, offset, bak);
        int totalCount = drugDao.queryTotalCount(bak);
        logger.info("页面查询到的总数："+totalCount);
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
    public Boolean crawlingData(String id, String keyword, User user) {
        logger.info("爬取信息:"+keyword);
        try {
           String currentWorkingDir = System.getProperty("user.dir");
           String pythonScriptPath = Paths.get(currentWorkingDir, "baiduDrug.py").toString();
//           // Python 脚本的路径
//           String pythonScriptPath = "C://Users//hammer//IdeaProjects//durg//src//main//resources//python//baiduDrug.py";
           // 构造执行命令的字符串
           String filename = String.valueOf(UUID.fastUUID())+".json";
           String command = "python " + pythonScriptPath+" "+keyword+" "+filename;
           logger.info(command);
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
            logger.info("Exit Code: " + exitCode);
           Thread.sleep(8000);
           //URL resource = InitDrugService.class.getClassLoader().getResource("python/"+filename);
           String absolutePath = Paths.get(currentWorkingDir, filename).toString();
           logger.info("Absolute Path: " + absolutePath);
           File file = new File(absolutePath);
           ObjectMapper objectMapper = new ObjectMapper();
           JsonNode jsonNode = objectMapper.readTree(file);

           String price1 = String.valueOf(jsonNode.get("price")).replaceAll("^\"|\"$", "");
           String img1 = String.valueOf(jsonNode.get("headerImages")).replaceAll("^\"|\"$", "");
           String bak = String.valueOf(jsonNode.get("indication")).replaceAll("^\"|\"$", "");
           String img2 = String.valueOf(jsonNode.get("img2")).replaceAll("^\"|\"$", "");
           String price2 = String.valueOf(jsonNode.get("price2")).replaceAll("^\"|\"$", "");
           String instructions = String.valueOf(jsonNode.get("resultSentence")).replaceAll("^\"|\"$", "");
           String currentDateTime = DateUtil.now();
           String time = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
           //还要插入日志
            if(user !=null ){
                logDao.insertLog(String.valueOf(UUID.fastUUID()), id, keyword, user.getId(), user.getUserName(), time, "curlDurgData");
            }
           drugDao.updateDrug(id, bak, time, instructions, price1, price2, img1, img2);
            Path path = Paths.get(absolutePath);
            // 删除文件
            Files.delete(path);
            logger.info("文件删除成功");
           return true;
       }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return false;
       }
    }

//    public static void main(String[] args) throws URISyntaxException, IOException {
//        String absolutePath = "C:\\Users\\hammer\\IdeaProjects\\durg\\f33227cf-a51e-4c99-aaca-a1feec5a390d.json";
//        String abs = "C:\\Users\\hammer\\IdeaProjects\\durg\\src\\main\\resources\\file\\drug.json";
//        System.out.println("Absolute Path: " + absolutePath);
//        File file = new File(absolutePath);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(file);
//        System.out.println(jsonNode.get("commonName"));
//        for (JsonNode item : jsonNode) {
//            System.out.println(item.get("commonName"));
//        }
//    }

    @Override
    public boolean insertDrug(String name, User user) {
// 获取当前时间字符串，默认格式为 "yyyy-MM-dd HH:mm:ss"
        String currentDateTime = DateUtil.now();
        logger.info("插入的Current Date and Time: " + currentDateTime);
        // 可以指定自定义的日期格式
        String customFormat = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
        String uuid = String.valueOf(UUID.fastUUID());
        drugDao.insertDrugName(uuid, name, customFormat);
        //还要插入日志
        logDao.insertLog(String.valueOf(UUID.fastUUID()), uuid, name, user.getId(), user.getUserName(), customFormat, "addDurg");
        return true;
    }

    @Override
    public boolean delDrug(String id, String name, User user) {
        String currentDateTime = DateUtil.now();
        // 可以指定自定义的日期格式
        String customFormat = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
        //还要插入日志
        logDao.insertLog(String.valueOf(UUID.fastUUID()), id, name, user.getId(), user.getUserName(), customFormat, "delDurg");
        return drugDao.delDrugName(id);
    }

    @Override
    public Drug getDurgById(String id, User user) {
        Drug drug = drugDao.getDurgById(id);
        String currentDateTime = DateUtil.now();
        // 可以指定自定义的日期格式
        String customFormat = DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss");
        //还要插入日志
        logDao.insertLog(String.valueOf(UUID.fastUUID()), id, drug.getName(), user.getId(), user.getUserName(), customFormat, "lookDurg");
        return drug;
    }

    @Override
    public Boolean crawlingAllData() {
        List<Drug> drugs = drugDao.queryForList();
        for(Drug drug: drugs){
            String id = drug.getId();
            String name = drug.getName();
            crawlingData(id, name, null);
        }
        return true;
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
