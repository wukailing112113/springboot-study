package springboot.pay.alipay.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import springboot.pay.alipay.ILauchPayPreProcessor;
import springboot.pay.alipay.IOrderService;
import springboot.pay.alipay.IPayClient;
import springboot.pay.alipay.http.IHttpClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sawyer on 2018/1/9.
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService,InitializingBean {


    protected Map<String, IPayClient> payClients = new HashMap<>();
    @Autowired
    protected IHttpClient httpClient;

    @Autowired(required = false)
    protected List<ILauchPayPreProcessor> launchPayPreProcessors = new ArrayList<>();

    @Autowired
    @Qualifier("newAlipayClient")
    private IPayClient newAlipayClient;


    @Override
    public String launchPay(PayVo vo)  throws Exception{
        launchPayPreProcessors.forEach(e-> e.process(vo));
        IPayClient payClient = payClients.get("NEWALIPAY");
        String result = "";
        if(payClient!=null){
            resolvePayVoParams(vo);
            result = payClient.buildRequestUrl(vo.getParms(), httpClient);
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        payClients.clear();
        payClients.put("NEWALIPAY",newAlipayClient);
    }

    private void resolvePayVoParams(PayVo vo){
        vo.getParms().put("notifyUrl", "");
        vo.getParms().put("paymentSuccessJumpToURL", "/pages/busi/success-pay.html");
        vo.getParms().put("returnUrl", "/pages/busi/success-pay.html");
        vo.getParms().put("showUrl", "/pages/busi/success-pay.html");
        vo.getParms().put("out_trade_no", "iba20180"+String.valueOf(new Date().getTime()));
        vo.getParms().put("total_amount", "0.01");
        vo.getParms().put("subject", "测试商品");
        vo.getParms().put("channel","5");
    }
}
