package springboot.pay.alipay.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import springboot.pay.alipay.Translator;

import java.util.Map;

import static springboot.pay.alipay.impl.Channel.PC;
import static springboot.pay.alipay.impl.Channel.WAP;
import static springboot.pay.alipay.impl.Channel.getChannel;

/**
 * Created by Sawyer on 2016/11/30.
 */
public class AlipayTradePayRequestTranslator implements Translator<AlipayRequest, Map<String, String>> {

    private static final Logger logger = Logger.getLogger(AlipayTradePayRequestTranslator.class);

//    private NewAlipayClient.Config config;
//    public AlipayTradePayRequestTranslator(NewAlipayClient.Config config) {
//        this.config = config;
//    }

    @Override
    public AlipayRequest translate(Map<String, String> requestParams) {
        Channel cn = getChannel(Byte.valueOf(String.valueOf(requestParams.get("channel"))));
        AlipayRequest alipayRequest = bindCommonField(null, requestParams);
        return injectBizContent(requestParams, cn, alipayRequest);
    }

    private AlipayRequest<? extends AlipayResponse> injectBizContent(final Map<String, String> requestParams, final Channel cn, Object alipayRequest) {
        if (cn == PC || cn == WAP) {
            AlipayTradeWapPayRequest alipayTradeWapPayRequest = (AlipayTradeWapPayRequest) alipayRequest;
            alipayTradeWapPayRequest.setBizContent(bizContent(requestParams, cn));
            return alipayTradeWapPayRequest;
        } else {
            AlipayTradeAppPayRequest alipayTradeAppPayRequest = (AlipayTradeAppPayRequest) alipayRequest;
            alipayTradeAppPayRequest.setBizContent(bizContent(requestParams, cn));
            return alipayTradeAppPayRequest;
        }
    }

    private String bizContent(final Map<String, String> requestParams, final Channel cn) {
        return new JSONObject() {{
            put("out_trade_no", requestParams.get("payno"));
            put("total_amount", requestParams.get("totalPrice"));
            put("subject", requestParams.get("subject"));
            put("seller_id", Config.partner);
            put("product_code", ((cn == PC || cn == WAP) ? "QUICK_WAP_PAY" : "QUICK_MSECURITY_PAY"));
            resolveTimeout();
        }

            private void resolveTimeout() {
                if(!StringUtils.isEmpty(requestParams.get("timeout"))) {
                    put("timeout_express", requestParams.get("timeout"));
                }
            }
        }.toJSONString();
    }

    public AlipayRequest bindCommonField(Channel cn, Map<String, String> requestParams) {
        AlipayRequest alipayRequest;
        alipayRequest = (cn == PC || cn == WAP )? new AlipayTradeWapPayRequest() : new AlipayTradeAppPayRequest();
        String url = requestParams.get("host");

//        if(CommonUtil.isNeedTransferToHttps(url, config.getSsl())){
//            url = url.replace("http://", "https://");
//        }

        alipayRequest.setReturnUrl(url + StringUtils.defaultIfEmpty(requestParams.get("paymentSuccessJumpToURL"), Config.returnUrl));
//        alipayRequest.setNotifyUrl(url + StringUtil.getNewUriWithConfiguredPrefix(cn == PC ? "" : "/app") + StringUtils.defaultIfEmpty(requestParams.get("notifyUrl"), config.getNotifyUrl()));//在公共参数中设置回跳和通知地址

        alipayRequest.setNotifyUrl(url + StringUtils.defaultIfEmpty(requestParams.get("notifyUrl"), Config.notifyUrl));


        logger.error("构造支付宝支付报文: ReturnUrl"+ alipayRequest.getReturnUrl());
        logger.error("构造支付宝支付报文: NotifyUrl"+ alipayRequest.getNotifyUrl());
        return alipayRequest;
    }
}
