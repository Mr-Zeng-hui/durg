package com.example.demo.Service;

import com.example.demo.Model.Drug;
import com.example.demo.Model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IDrugSerivce {

    Map<String, Object> queryForPage(int pageSize, int offset, String bak);

    int queryTotalCount();

    Boolean crawlingData(String id, String keyword, User user);

    boolean insertDrug(String name, User user);

    boolean delDrug(String id, String name, User user);

    Drug getDurgById(String id, User user);

    Boolean crawlingAllData();

}
