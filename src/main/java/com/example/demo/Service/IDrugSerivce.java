package com.example.demo.Service;

import com.example.demo.Model.Drug;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IDrugSerivce {

    Map<String, Object> queryForPage(int pageSize, int offset, String bak);

    int queryTotalCount();

    Boolean crawlingData(String id, String keyword);

    boolean insertDrug(String name);

    boolean delDrug(String id, String name);

    Drug getDurgById(String id);

    Boolean crawlingAllData();

}
