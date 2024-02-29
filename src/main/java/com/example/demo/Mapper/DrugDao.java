package com.example.demo.Mapper;


import com.example.demo.Model.Drug;
import com.example.demo.Model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface DrugDao {

    @Select({
            "<script>",
            "SELECT * FROM drug_set",
            "<if test='bak != null'>",
            "WHERE bak like #{bak}",
            "</if>",
            "LIMIT #{pageSize} OFFSET #{offset}",
            "</script>"
    })
    List<Drug> queryForPage(@Param("pageSize") int pageSize, @Param("offset") int offset ,@Param("bak") String bak);

    // 查询总记录数
    @Select("SELECT COUNT(*) FROM drug_set")
    int queryTotalCount();
}
