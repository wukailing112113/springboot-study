package springboot.aop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springboot.aop.enty.User;
import springboot.aop.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add",method =RequestMethod.POST)
    public String addUser(@RequestBody User user){
        userService.save(user);
        return "success";
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        return "test";
    }

    public static void main(String[] args) {
        long a =40l;
        System.out.println(Float.valueOf(a));
    }
}
