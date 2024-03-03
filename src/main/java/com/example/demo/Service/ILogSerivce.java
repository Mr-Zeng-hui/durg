package com.example.demo.Service;

import com.example.demo.Model.User;

import java.util.Map;

public interface ILogSerivce {

    Map<String, Object> queryForPage(int pageSize, int offset, String bak, User user);

    int queryTotalCount();

    boolean insertLog(String durgid, String durgname, String userid, String username, String time, String type);

}
