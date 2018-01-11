package springboot.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springboot.pay.alipay.IOrderService;
import springboot.pay.alipay.impl.PayVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.alipay.api.AlipayConstants.CHARSET;
import static com.alipay.api.AlipayConstants.CHARSET_UTF8;

/**
 * Created by Sawyer on 2018/1/9.
 */
@Controller
public class AlipayController {

    @Autowired
    private IOrderService orderService;


    @PostMapping(value = "/order/pay",produces = "application/json;charset=UTF-8")
    public void pay(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception{
        PayVo vo = new PayVo();
        String form  = orderService.launchPay(vo);
        httpResponse.setContentType("text/html;charset=" + CHARSET_UTF8);
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @RequestMapping(value = "/pay",method = RequestMethod.GET)
    public String pay(Map<String,Object> model){
        model.put("payType",0);
        return "pay";
    }
    @RequestMapping("/welcome")
    public String welcome(Map<String, Object> model) {
        model.put("message", "kling:-w");
        return "welcome";
    }

}
