package com.example.demo.Service;


import cn.zeng.email.config.MailConfig;
import cn.zeng.email.constant.EmailContentTypeEnum;
import cn.zeng.email.constant.SmtpHostEnum;
import cn.zeng.email.core.MiniEmail;
import cn.zeng.email.core.MiniEmailFactoryBuilder;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Mapper.EmailMapper;
import com.example.demo.Model.Email;
import com.example.demo.util.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class EmailSendService {
    private static Logger logger = LoggerFactory.getLogger(EmailSendService.class);


    private final EmailMapper mapper;

    /**
     * 按照前端页面上的格式来
     * ['自定义','网易163邮箱','新浪邮箱','企业邮箱','QQ邮箱','移动139邮箱','微软outlook邮箱'];
     */
    private static SmtpHostEnum[] smtpHost = {
             SmtpHostEnum.SMTP_163
            ,SmtpHostEnum.SMTP_SINA
            ,SmtpHostEnum.SMTP_ENTERPRISE_QQ
            ,SmtpHostEnum.SMTP_QQ
            ,SmtpHostEnum.SMTP_139
            ,SmtpHostEnum.SMTP_OUTLOOK
    };

    @Autowired
    public EmailSendService(EmailMapper mapper) {
        this.mapper = mapper;
    }


    public JSONObject sendEmail(JSONObject jsonInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "发送成功");
        try {
            String content;
            String emailUser;
            String emailPwd;
            int emailType;
            Email email = (Email) CacheUtil.get("Email");
            if (email == null) {
                email = mapper.select(1);
            }
            String randomCode = getRandomCode();
            content = email.getTemplate().replace("[CODE]", randomCode);
            emailUser = email.getEmail();
            emailPwd = email.getEmailPwd();
            emailType= Integer.valueOf(email.getEmailType());
            String to = jsonInfo.getString("email_acc");
            MiniEmail miniEmail = new MiniEmailFactoryBuilder().build(MailConfig.config(emailUser, emailPwd)
                    .setMailDebug(Boolean.TRUE)
                    .setSenderNickname("药品管理系统")
                    .setMailSmtpHost(smtpHost[emailType])
            ).init();

            List<String> sendSuccessToList = miniEmail
                    .send(to.split(","), "验证码", EmailContentTypeEnum.HTML, content);
            logger.info("send success to = " + sendSuccessToList);
            //存入缓存 时间为5分钟
            CacheUtil.put("CODE_" + to, randomCode, 5);
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


    public JSONObject updateEmail(Email email) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "修改成功");
        try {
            email.setId("1");
            int i = mapper.updateValue(email);
            if (i == 1) {
                CacheUtil.put("Email", email, 100);
            }else {
                jsonObject.put("code", 400);
                jsonObject.put("msg", "修改失败");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            jsonObject.put("code", 400);
            jsonObject.put("msg", e.getMessage());
        }

        return jsonObject;
    }


    public Email selectAllEmail() {
        Email email = mapper.select(1);

        return email;
    }


}
