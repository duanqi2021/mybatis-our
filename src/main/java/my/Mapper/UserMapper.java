package my.Mapper;

import my.domain.User;
import mybatis.annotation.Param;
import mybatis.annotation.Select;

import java.util.List;

public interface UserMapper {
    @Select("select * from user where USER_Sex=#{sex} ")
    public List<User> getUser(@Param("sex") String sex);

    @Select("select USER_Id,USER_Name from user where USER_Id=#{id}")
    public User getUserById(@Param("id") String id);
}
