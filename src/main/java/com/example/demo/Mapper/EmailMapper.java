package com.example.demo.Mapper;

import com.example.demo.Model.Email;
import com.example.demo.Model.HelloModel;
import com.example.demo.Model.User;
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
    @Update("UPDATE durg_config SET id=#{email.id}, userName=#{email.userName} " +
            ", emailPwd=#{email.emailPwd} , email=#{email.email} , template=#{email.template}" +
            ", emailType=#{email.emailType}  WHERE id=#{email.id}")
    int updateValue(@Param("email") Email email);

}