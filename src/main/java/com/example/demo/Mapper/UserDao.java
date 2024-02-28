package com.example.demo.Mapper;


import com.example.demo.Model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface UserDao {
    /**
     * 验证登录
     * @param username
     * @param password
     * @return
     */
//    User queryByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM user_info WHERE USER_NAME=#{username} AND PASSWORD=#{password}")
    User queryByUsernameAndPassword( String username,  String password);
}
