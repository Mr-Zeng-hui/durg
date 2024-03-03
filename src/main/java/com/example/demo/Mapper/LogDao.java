package com.example.demo.Mapper;


import com.example.demo.Model.Drug;
import com.example.demo.Model.Log;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface LogDao {

    @Select({
            "<script>",
            "SELECT * FROM log",
            "<if test='bak != null and bak != \"\"'>",
            "WHERE durgname LIKE '%' || #{bak} || '%'",
            "</if>",
            " order by time desc LIMIT #{pageSize} OFFSET #{offset}",
            "</script>"
    })
    List<Log> queryForPage(@Param("pageSize") int pageSize, @Param("offset") int offset, @Param("bak") String bak);

    // 查询总记录数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM log",
            "<if test='bak != null and bak != \"\"'>",
            "WHERE durgname LIKE '%' || #{bak} || '%'",
            "</if>",
            "</script>"
    })
    int queryTotalCount(@Param("bak") String bak);

    @Insert({"INSERT INTO log (id, durgid , durgname, userid, username, time, type) VALUES (#{id}, #{durgid}, #{durgname}, #{userid}, #{username}, #{time}, #{type})"})
    boolean insertLog(@Param("id") String id, @Param("durgid") String durgid, @Param("durgname") String durgname ,@Param("userid") String userid,@Param("username") String username, @Param("time") String time, @Param("type") String type);


    @Select({
            "<script>",
            "SELECT t1.id,t1.durgid,t1.durgname,t1.userid,t1.type,t1.time,t2.userName as username FROM log t1",
            " join durg_user t2 on t2.id = t1.userid ",
            "WHERE t1.userid = #{userid} ",
            "<if test='bak != null and bak != \"\"'>",
            "and t1.durgname LIKE '%' || #{bak} || '%'",
            "</if>",
            " order by t1.time desc LIMIT #{pageSize} OFFSET #{offset}",
            "</script>"
    })
    List<Log> queryForPageByUserId(@Param("pageSize") int pageSize, @Param("offset") int offset, @Param("bak") String bak, @Param("userid") String userid);

    // 查询总记录数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM log",
            "WHERE userid = #{userid} ",
            "<if test='bak != null and bak != \"\"'>",
            "and durgname LIKE '%' || #{bak} || '%'",
            "</if>",
            "</script>"
    })
    int queryTotalCountByUserId(@Param("bak") String bak, @Param("userid") String userid);
}
