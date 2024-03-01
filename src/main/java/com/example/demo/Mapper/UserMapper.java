package com.example.demo.Mapper;


import com.example.demo.Model.HelloModel;
import com.example.demo.Model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;


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

    @Select("SELECT * FROM durg_user WHERE email=#{username} AND password=#{password} and state ='1'")
    User queryByUsernameAndPassword( String username,  String password);


    @Select("SELECT * FROM durg_user WHERE email=#{emailAccount} and state ='1' ")
    User checkUserEmail( String emailAccount);


//    @Insert("INSERT INTO durg_user(key, value) VALUES(#{key}, #{value})")
//    int insert(User user);

    @Insert("INSERT INTO durg_user(id,userName,password,email,type,state,securityIssues,errorCount, answer) " +
            "VALUES(#{user.id}, #{user.userName}, #{user.password}, #{user.email}, #{user.type}, #{user.state}" +
            ", #{user.securityIssues}, #{user.errorCount}, #{user.answer})")
    int insert(@Param("user") User user);

//    @Update("UPDATE durg_user SET value=#{value} WHERE userNmae=#{userName}")
//    int resetPassword(User user);

    @Update("UPDATE durg_user SET password = #{user.password} WHERE id = #{user.id}")
    int resetPassword(@Param("user") User user);


    // 根据 ID 删除
    @Delete("DELETE FROM durg_user WHERE id = #{id}")
    int deleteUser(String id);

    // 查询全部
    @Select("SELECT * FROM durg_user where 1 =1  ")
    List<User> selectAll(String param);



}
