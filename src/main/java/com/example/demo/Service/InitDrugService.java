package com.example.demo.Service;

import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InitDrugService {

    public static void main(String[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            URL resource = InitDrugService.class.getClassLoader().getResource("file/drug.json");
            File file = new File(resource.toURI());
            JsonNode jsonNode = objectMapper.readTree(file);
            System.out.println(jsonNode);
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D://work//myDb");
            insertData(connection, jsonNode);
            connection.close();
            System.out.println("Data inserted into SQLite successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertData(Connection connection, JsonNode jsonNode) throws Exception {
        String insertDataSQL = "INSERT INTO drug_set (ID, NAME, TYPE) VALUES (?, ?, ?);";
        System.out.println(jsonNode.toString());

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {
            for (JsonNode item : jsonNode) {
                System.out.println(item);
                preparedStatement.setString(1, String.valueOf(UUID.fastUUID()));
                preparedStatement.setString(2, item.get("mc").textValue());
                if("甲".equals(String.valueOf(item.get("lx")))){
                    preparedStatement.setString(3, "1");
                }else{
                    preparedStatement.setString(3, "2");
                }
                preparedStatement.executeUpdate();
            }
        }
    }

    private static void copyResourceFile(String resourcePath, Path destinationPath) throws Exception {
        try (InputStream inputStream = InitDrugService.class.getResourceAsStream(resourcePath)) {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
