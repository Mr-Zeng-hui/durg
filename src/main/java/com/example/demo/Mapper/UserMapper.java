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

    @Select("SELECT * FROM durg_user WHERE userName=#{username} AND password=#{password} and state ='1'")
    User queryByUsernameAndPassword( String username,  String password);


    @Select("SELECT * FROM durg_user WHERE email=#{emailAccount} and state ='1' ")
    User checkUserEmail( String emailAccount);


    @Insert("INSERT INTO durg_user(key, value) VALUES(#{key}, #{value})")
    int insert(User user);

    @Update("UPDATE durg_user SET value=#{value} WHERE userNmae=#{userName}")
    int resetPassword(User user);


    // 根据 ID 删除
    @Delete("DELETE FROM durg_user WHERE userName=#{userName}")
    int deleteUser(String userName);

    // 查询全部
    @Select("SELECT * FROM durg_user where 1 =1 #{param} ")
    List<User> selectAll(String param);
}
