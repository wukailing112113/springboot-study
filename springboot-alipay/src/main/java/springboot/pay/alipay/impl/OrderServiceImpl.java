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

    /**
     * 注入支付终端，如支付宝，微信或者银联等
     */
    @Autowired
    @Qualifier("newAlipayClient")
    private IPayClient newAlipayClient;


    @Override
    public String launchPay(PayVo vo)  throws Exception{
        launchPayPreProcessors.forEach(e-> e.process(vo));
        // 获取支付终端:支付宝，新版支付宝，微信....
        IPayClient payClient = payClients.get(PayType.getPayType(vo.getPayType()).toString());
        String result = "";
        if(payClient!=null){
            resolvePayVoParams(vo);
            result = payClient.buildRequestUrl(vo.getParms(), httpClient);
        }
        return result;
    }

    /**
     * Tomcat 启动的时候初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        payClients.clear();
        payClients.put("NEWALIPAY",newAlipayClient);
        payClients.put("ALIPAY",null);
    }

    private void resolvePayVoParams(PayVo vo){
        vo.getParms().put("host", "http://kling.ngrok.ibaboss.com");
        vo.getParms().put("paymentSuccessJumpToURL", "");
        vo.getParms().put("returnUrl", "");
        vo.getParms().put("showUrl", "/pages/busi/success-pay.html");
        vo.getParms().put("out_trade_no", "iba20180"+String.valueOf(System.currentTimeMillis()));
        vo.getParms().put("total_amount", "0.01");
        vo.getParms().put("subject", "测试商品");
        vo.getParms().put("timeout", "1m");
    }
}
