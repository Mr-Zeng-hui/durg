package com.example.demo.Service;

import com.example.demo.Model.Drug;

import java.util.List;
import java.util.Map;

public interface IDrugSerivce {

    Map<String, Object> queryForPage(int pageSize, int offset);

    int queryTotalCount();

    Boolean crawlingData(String id);
}
