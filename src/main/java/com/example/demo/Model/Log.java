package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private String id;
    private String durgid;
    private String durgname;
    private String userid;
    private String username;
    private String time;
    private String type;
}
