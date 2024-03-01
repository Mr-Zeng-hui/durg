package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //登录账号（邮箱）、用户名、密码、是否启用、是否管理员、密保问题、密保答案、用户ID
    private String id;
    private String userName;
    private String password;
    private String email;
    private String type;
    private String state;
    private String securityIssues;
    private String errorCount;
    private String answer;

}
