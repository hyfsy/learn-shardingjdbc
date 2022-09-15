package com.hyf.shardingsphere.mapper;

import com.hyf.shardingsphere.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/09/14
 */
@Mapper
public interface UserMapper {

    @Select("select * from user where user_id = #{id}")
    User getById(@Param("id") Integer id);

    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    @Insert("insert into user (name) values (#{user.name})")
    boolean insert(@Param("user") User user);

    @Insert("insert into user (id, name) values (#{user.user_id}, #{user.name})")
    boolean insertGene(@Param("user") User user);

    @Select("select * from user")
    List<User> list();

}
