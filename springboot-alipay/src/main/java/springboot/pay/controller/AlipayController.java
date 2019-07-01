package springboot.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springboot.pay.service.PayService;
import springboot.pay.vo.PayRequestVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * Created by Sawyer on 2018/1/9.
 */
@Controller
@RequestMapping("/pay")
public class AlipayController {

    private static final String CHARSET_UTF8 = "utf-8";

    @Autowired
    private PayService payService;


    /**
     * 所有支付方式的入口：如支付宝H5,支付宝APP，微信等支付方式
     * 只完成了支付的唤起，支付后的回调还没有实现--2018-1-11
     * @param request
     * @param httpResponse
     * @throws Exception
     */
    @PostMapping(value = "/alipay",produces = "application/json;charset=UTF-8")
    public void pay(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception{
        String form  = payService.payInMobileSite(request);
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
    @RequestMapping(value = "/welcome",method = RequestMethod.GET)
    public String welcome(Map<String, Object> model) {
        model.put("message", "kling:-w");
        return "welcome";
    }

    @RequestMapping(value = "/call-back",method = RequestMethod.POST)
    public void callbackUrl(HttpServletRequest request,HttpServletResponse response) {
        System.out.println(request.getParameterMap());
        System.out.println("支付宝回调地址！！！");
        try {
            response.getWriter().write("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
