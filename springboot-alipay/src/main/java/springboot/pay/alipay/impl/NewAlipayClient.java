package springboot.pay.alipay.impl;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import springboot.pay.alipay.IPayClient;
import springboot.pay.alipay.http.IHttpClient;

import java.util.Map;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static springboot.pay.alipay.impl.Channel.PC;
import static springboot.pay.alipay.impl.Channel.WAP;
import static springboot.pay.alipay.impl.Channel.getChannel;


/**
 * alipay的新接口支持
 * Created by Sawyer on 2016/11/29.
 */
@Component("newAlipayClient")
public class NewAlipayClient implements IPayClient {

    private Logger logger = Logger.getLogger(NewAlipayClient.class);

    public static final String input_charset = "utf-8";
    public static final String ALIPAY_GATEWAY_NEW = "https://openapi.alipay.com/gateway.do";
    private AlipayClient alipayClient;

    @Override
    public void setConfig(String json) {
        //config = JSONObject.parseObject(json, Config.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String buildRequestUrl(Map<String, String> requestParams, IHttpClient httpClient) {
        if (alipayClient == null) {
            alipayClient = new DefaultAlipayClient(ALIPAY_GATEWAY_NEW,Config.appId , springboot.pay.alipay.impl.Config.privateKey, "json", input_charset, springboot.pay.alipay.impl.Config.publicKey);
        }
        try {
            Channel channel = getChannel(Byte.valueOf(String.valueOf(requestParams.get("channel"))));
            AlipayRequest alipayTradePayRequest = new AlipayTradePayRequestTranslator().translate(requestParams);
            if (channel == PC || channel == WAP) {
                return alipayClient.pageExecute((AlipayTradeWapPayRequest)alipayTradePayRequest).getBody();
            } else {
                return alipayClient.sdkExecute((AlipayTradeAppPayRequest)alipayTradePayRequest).getBody();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return EMPTY;
    }


    @Override
    public Boolean verifyCallback(String json) {
        Map<String, String> params = JSONObject.parseObject(json, Map.class);
        try {
            return AlipaySignature.rsaCheckV1(params, Config.publicKey, input_charset); //调用SDK验证签名
        } catch (AlipayApiException e) {
            logger.error("支付宝回调验证失败：" + json);
            e.printStackTrace();
            return false;
        }
    }

}
