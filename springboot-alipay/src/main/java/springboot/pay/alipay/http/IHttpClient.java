package springboot.pay.alipay.http;

import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

public interface IHttpClient {


	String get(String url, String encoding) throws Exception;

	String get(String url, Map<String, String> params, String encoding) throws Exception;

	String get(String baseUrl, Map<String, String> headers, Map<String, String> params, String encoding) throws Exception;

	String post(String url, String encoding) throws Exception;

	String post(String url, Map<String, String> params, String encoding) throws Exception;

	String post(String url, String params, String encoding) throws Exception;

	<T> String postXml(String url, T obj, String encoding) throws Exception;

	<T> String postXml(CloseableHttpClient httpClient, String url, T obj, String encoding) throws Exception;

	public String postJson(String url, String json, String encoding) throws Exception;

}
