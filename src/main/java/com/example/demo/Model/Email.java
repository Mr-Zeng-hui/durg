package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    private String id;
    private String userName;
    private String emailPwd;
    private String email;
    private String template;
    private String emailType;
}
