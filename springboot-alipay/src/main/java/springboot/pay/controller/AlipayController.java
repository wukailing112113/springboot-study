package springboot.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springboot.pay.alipay.IOrderService;
import springboot.pay.alipay.impl.PayVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sawyer on 2018/1/9.
 */
@RestController
public class AlipayController {

    @Autowired
    private IOrderService orderService;


    @PostMapping(value = "/order/pay",produces = "application/json;charset=utf-8")
    public void pay(HttpServletRequest request, HttpServletResponse response) throws Exception{
        PayVo vo = new PayVo();
        String requestUrl  = orderService.launchPay(vo);
        response.getWriter().write(requestUrl);
    }

    @RequestMapping(value = "/pay",method = RequestMethod.GET)
    public String pay(){
        return "pay";
    }

}
