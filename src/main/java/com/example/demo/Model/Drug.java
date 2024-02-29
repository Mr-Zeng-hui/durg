package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drug {
    private String id;
    private String name;
    private String bak;
    private String instructions;
    private String price1;
    private String img1;
    private String price2;
    private String img2;
    private String bigname;
}
