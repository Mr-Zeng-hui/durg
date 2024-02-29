package com.example.demo.Service.imp;

import com.example.demo.Mapper.DrugDao;
import com.example.demo.Model.Drug;
import com.example.demo.Service.IDrugSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DrugServiceImpl implements IDrugSerivce {
    @Autowired
    private DrugDao drugDao;

    @Override
    public Map<String, Object> queryForPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Drug> drugList = drugDao.queryForPage(pageSize, offset);
        int totalCount = drugDao.queryTotalCount();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("msg", "");
        result.put("count", totalCount);
        result.put("data", drugList);
        return result;
    }

    @Override
    public int queryTotalCount() {
        return drugDao.queryTotalCount();
    }

    @Override
    public Boolean crawlingData(String id) {
        return null;
    }
}
