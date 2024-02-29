package com.example.demo.Mapper;


import com.example.demo.Model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface UserMapper {
    /**
     * 验证登录
     * @param username
     * @param password
     * @return
     */
//    User queryByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("SELECT id ,USER_NAME AS userName ,PASSWORD AS password, EMAIL AS email,TYPE AS type ,STATE AS state,SECURITY_ISSUES AS securityIssues,ERROR_COUNT as errorCount ,ANSWER as answer FROM user_info WHERE USER_NAME=#{username} AND PASSWORD=#{password}")
    User queryByUsernameAndPassword( String username,  String password);
}
