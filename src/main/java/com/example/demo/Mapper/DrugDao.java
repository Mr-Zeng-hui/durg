package com.example.demo.Mapper;


import com.example.demo.Model.Drug;
import com.example.demo.Model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface DrugDao {

    @Select({
            "<script>",
            "SELECT * FROM drug_set",
            "<if test='bak != null and bak != \"\"'>",
            "WHERE bak LIKE '%' || #{bak} || '%'",
            " or name LIKE '%' || #{bak} || '%'",
            "</if>",
            " order by time desc LIMIT #{pageSize} OFFSET #{offset}",
            "</script>"
    })
    List<Drug> queryForPage(@Param("pageSize") int pageSize, @Param("offset") int offset ,@Param("bak") String bak);

    // 查询总记录数
    @Select("SELECT COUNT(*) FROM drug_set")
    int queryTotalCount();

    @Insert({"INSERT INTO drug_set (id, name , type, time) VALUES (#{id}, #{name}, '1', #{time})"})
    boolean insertDrugName(@Param("id") String id, @Param("name") String name,  @Param("time") String time);

    @Delete({"Delete from  drug_set where id =  #{id}"})
    boolean delDrugName(@Param("id") String id);
}
