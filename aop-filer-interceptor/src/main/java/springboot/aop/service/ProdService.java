package springboot.aop.service;


import org.springframework.stereotype.Component;
import springboot.aop.enty.Prod;

@Component
public class ProdService {

    public void save(Prod prod){
        System.out.println(prod.getName());
    }
}
