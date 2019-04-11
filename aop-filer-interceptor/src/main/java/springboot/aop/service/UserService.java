package springboot.aop.service;

import org.springframework.stereotype.Component;
import springboot.aop.enty.User;

@Component
public class UserService {

    public void save(User user){
        System.out.println(user.getStartTime());
    }
}
