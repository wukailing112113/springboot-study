package springboot.pay.alipay;

import springboot.pay.alipay.http.IHttpClient;

import java.util.Map;

public interface IPayClient {

    /**
     * 设置配置参数
     */
    void setConfig(String json);

    String buildRequestUrl(Map<String, String> params, IHttpClient httpClient) throws Exception;

    Boolean verifyCallback(String json);


}

