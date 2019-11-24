package springbootjwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//https://www.jianshu.com/p/e88d3f8151db

/**
 * spring boot 继承jwt验证登录授权
 */
@SpringBootApplication
@MapperScan("springbootjwt.mapper")
public class SpringbootJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJwtApplication.class, args);
    }

}
