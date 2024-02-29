package com.example.demo.Service;

import cn.zeng.email.config.MailConfig;
import cn.zeng.email.constant.EmailContentTypeEnum;
import cn.zeng.email.constant.SmtpHostEnum;
import cn.zeng.email.core.MiniEmail;
import cn.zeng.email.core.MiniEmailFactoryBuilder;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;


@Service
public class EmailSendService {
    private static Logger logger = LoggerFactory.getLogger(EmailSendService.class);



    public JSONObject sendEmail(JSONObject jsonInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "发送成功");
        try {
            String to = jsonInfo.getString("to");
            String content = getRandomCode();
            MiniEmail miniEmail = new MiniEmailFactoryBuilder().build(MailConfig.config("tanzhenxiang007@163.com", "VZYGFCTVSBZIHQIK")
                    .setMailDebug(Boolean.TRUE)
                    .setSenderNickname("药品管理系统")
                    .setMailSmtpHost(SmtpHostEnum.SMTP_163)
            ).init();

            List<String> sendSuccessToList = miniEmail
                    .send(to.split(","), "验证码", EmailContentTypeEnum.HTML, content);
            logger.info("send success to = " + sendSuccessToList);
            //存入缓存 时间为5分钟
            CacheUtil.put("CODE_"+to, content, 5);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            jsonObject.put("code", 400);
            jsonObject.put("msg", e.getMessage());
        }

        return jsonObject;
    }

    public static String getRandomCode() {
        // 创建Random对象
        Random random = new Random();

        // 生成一个0到9999之间的随机整数
        int randomNumber = random.nextInt(10000);

        // 确保生成的数字是四位数，如果不足四位前面补零
        String formattedNumber = String.format("%04d", randomNumber);
        return formattedNumber;
    }






}
