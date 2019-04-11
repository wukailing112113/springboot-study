package springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/** 模板使用控制器
 * Created by kling on 2018/1/11.
 */
@Controller
public class WelcomeController {

    private String message = "Hello World";

    @RequestMapping("/welcome")
    public String welcome(Map<String, Object> model) {
        model.put("message", this.message);
        return "welcome";
    }

    public static void main(String[] args) {
        String a = "4";
        String[] split = a.split(",");
        System.out.println(split.length);
    }
}
