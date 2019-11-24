package springbootjwt.mapper;

import org.apache.ibatis.annotations.Param;
import springbootjwt.entity.User;

public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findUserById(@Param("Id") String Id);
}
