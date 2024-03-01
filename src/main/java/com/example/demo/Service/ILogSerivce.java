package com.example.demo.Service;

import java.util.Map;

public interface ILogSerivce {

    Map<String, Object> queryForPage(int pageSize, int offset, String bak);

    int queryTotalCount();

    boolean insertLog(String durgid, String durgname, String userid, String username, String time, String type);

}
