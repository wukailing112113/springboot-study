package org.spring.springboot.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot HelloWorld 案例
 *
 * Created by bysocket on 16/4/26.
 */
@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public String sayHello() {
        return "Hello,World!";
    }


    public static void main(String[] args) {
        String a = "/1/1.jpg";
        String[] split = a.split("/");
        for(int i=0;i<split.length;i++){
            System.out.println(i+"--"+split[i]);
        }
    }
}
