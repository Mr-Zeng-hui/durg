package com.example.demo.Mapper;

import com.example.demo.Model.Email;
import com.example.demo.Model.HelloModel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface EmailMapper {


    // 根据 ID 查询
    @Select("SELECT * FROM durg_config WHERE id=#{id}")
    Email select(int id);

    // 更新 value
    @Update("UPDATE durg_config SET value=#{value} WHERE id=#{id}")
    int updateValue(Email email);

}