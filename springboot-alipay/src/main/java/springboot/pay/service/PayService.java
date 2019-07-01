package springboot.pay.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import springboot.pay.config.Config;
import springboot.pay.utils.JsonUtil;
import springboot.pay.vo.PayRequestVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class PayService {
    private static final Log log = LogFactory.getLog(PayService.class);

    public String payInMobileSite(HttpServletRequest request){
        log.debug("payInMobileSite request=\n" + request.toString());
        String form = "<font style='color: red'>请求支付宝超时,请稍后再试!</font>";
        try {

            PayRequestVo payRequestVo = new PayRequestVo();
            constructionBizParmeters(request,payRequestVo);
            payRequestVo.setProduct_code(System.currentTimeMillis()+"-iba");

            String bizContent = JsonUtil.beanToJsontring(payRequestVo);
            AlipayClient alipayClient = new DefaultAlipayClient(Config.alipaydevApi,
                    Config.appId, Config.privateKey, Config.format, Config.encode, Config.publicKey,Config.signType);

            // 调用支付宝的sdk,唤起支付页面
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setReturnUrl(Config.returnUrl);
            alipayRequest.setNotifyUrl(Config.notifyUrl);
            alipayRequest.setBizContent(bizContent);
            form = alipayClient.pageExecute(alipayRequest).getBody();
            System.out.println("form==="+form);
        }catch (Exception e){
            e.printStackTrace();
        }
        return form;
    }


    /**
     * 构造支付宝请求的业务参数
     * @param request
     * @param payRequestVo
     */
    private void constructionBizParmeters(HttpServletRequest request, PayRequestVo payRequestVo) {
        if(Objects.nonNull(request.getParameter("outTradeNo"))){
            payRequestVo.setOut_trade_no(request.getParameter("outTradeNo"));
        }
        if(Objects.nonNull(request.getParameter("subject"))){
            payRequestVo.setSubject(request.getParameter("subject"));
        }
        if(Objects.nonNull(request.getParameter("totalAmount"))){
            payRequestVo.setTotal_amount(request.getParameter("totalAmount"));
        }
        if(Objects.nonNull(request.getParameter("body"))){
            payRequestVo.setBody(request.getParameter("body"));
        }
        if(Objects.nonNull(request.getParameter("sellerId"))){
            payRequestVo.setSeller_id(request.getParameter("sellerId"));
        }
    }

}
