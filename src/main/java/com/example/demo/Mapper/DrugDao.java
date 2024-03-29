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
            "SELECT * FROM durg_set",
            "<if test='bak != null and bak != \"\"'>",
            "WHERE bak LIKE '%' || #{bak} || '%'",
            " or name LIKE '%' || #{bak} || '%'",
            "</if>",
            " order by time desc LIMIT #{pageSize} OFFSET #{offset}",
            "</script>"
    })
    List<Drug> queryForPage(@Param("pageSize") int pageSize, @Param("offset") int offset ,@Param("bak") String bak);

    // 查询总记录数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM durg_set",
            "<if test='bak != null and bak != \"\"'>",
            "WHERE bak LIKE '%' || #{bak} || '%'",
            "</if>",
            "</script>"
    })
    int queryTotalCount(@Param("bak") String bak);

    @Insert({"INSERT INTO durg_set (id, name , type, time) VALUES (#{id}, #{name}, '1', #{time})"})
    boolean insertDrugName(@Param("id") String id, @Param("name") String name,  @Param("time") String time);

    @Delete({"Delete from  durg_set where id =  #{id}"})
    boolean delDrugName(@Param("id") String id);

    @Update({"UPDATE durg_set SET bak=#{bak}, instructions=#{instructions}, time=#{time}, price1=#{price1}, price2=#{price2}, img1=#{img1}, img2=#{img2} WHERE id=#{id}"})
    boolean updateDrug(@Param("id") String id, @Param("bak") String bak,  @Param("time") String time,  @Param("instructions") String instructions,  @Param("price1") String price1,  @Param("price2") String price2,  @Param("img1") String img1,  @Param("img2") String img2);

    @Select({"select * from durg_set where id = #{id}"})
    Drug getDurgById(@Param("id") String id);

    @Select({"SELECT * FROM durg_set where price1 is null or price2 is null"})
    List<Drug> queryForList();

}
