package springboot.aop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springboot.aop.enty.Prod;
import springboot.aop.enty.User;
import springboot.aop.service.ProdService;
import springboot.aop.service.UserService;

@RestController
@RequestMapping("/prod")
public class ProdController {

    @Autowired
    private ProdService prodService;

    @RequestMapping(value = "/add",method =RequestMethod.POST)
    public String addProd(@RequestBody Prod prod){
        prodService.save(prod);
        return "success";
    }

}
