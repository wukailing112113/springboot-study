package org.spring.springboot.http;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;


@Component
public class HttpClient implements IHttpClient {

	@Autowired
	HttpClientBuilder httpClientBuilder;

	@Autowired
	RequestConfig requestConfig;

	private static final Logger logger = Logger.getLogger(HttpClient.class);

	/**
	 * 
	 * @param request
	 */
	private void abortRequest(HttpUriRequest request) {
		if (request != null) {
			request.abort();
		}
	}

	/**
	 * 
	 * @param something
	 *            请求/连接
	 */
	private void close(Closeable something) {
		if (something != null) {
			try {
				something.close();
			} catch (IOException e) {
			}
		}
	}

	private String executeHttpRequest(HttpRequestBase request, String encoding) throws Exception {
		return executeHttpRequest(request, encoding, null);
	}

	private String executeHttpRequest(HttpRequestBase request, String encoding, CloseableHttpClient httpClient) throws Exception {
		if(Objects.isNull(httpClient)) {
			httpClient = httpClientBuilder.build();
		}
		CloseableHttpResponse response = null;
		String result = null;
		try {
			request.setConfig(requestConfig);
			response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			// EntityUtils中会关闭流，触发释放连接资源的操作
			result = EntityUtils.toString(entity, encoding);
			EntityUtils.consume(entity);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return result;
			}else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN){
				abortRequest(request);
				return "{ 'resultCode': '403', 'resultDesc': 'Forbidden' }";
			}
			abortRequest(request);
		} catch (Exception e) {
			abortRequest(request);
			throw new Exception("http client " + request.getMethod() + "请求失败！", e);
		} finally {
			close(response);
		}
		return result;
	}

	public String get(String url, String encoding) throws Exception {
		if (!checkUrl(url)) {
			logger.error("url匹配失败！" + url);
			return null;
		}
		HttpGet httpGet = null;
		httpGet = new HttpGet(url);
		return executeHttpRequest(httpGet, encoding);
	}

	public String get(String url, Map<String, String> params, String encoding) throws Exception {
		String finalURL = getFinalURL(url, params);
		return get(finalURL, encoding);
	}

	public String get(String baseUrl,Map<String, String> headers, Map<String, String> params, String encoding) throws Exception {
		String finalURL = getFinalURL(baseUrl, params);
		if (!checkUrl(finalURL)) {
			logger.error("url匹配失败！" + finalURL);
			return null;
		}
		HttpGet httpGet = new HttpGet(finalURL);
		for (Entry<String, String> e : headers.entrySet()) {
			httpGet.addHeader(e.getKey(), e.getValue());
		}
		return executeHttpRequest(httpGet, encoding);
	}

	private String getFinalURL(String baseUrl, Map<String, String> params) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(baseUrl);
		for (String key : params.keySet()) {
			uriBuilder.addParameter(key, params.get(key));
			logger.debug("key=" + key + ",val=" + params.get(key));
		}
		return uriBuilder.build().toString();
	}

	public String post(String url, Map<String, String> params, String encoding) throws Exception {
		if (!checkUrl(url)) {
			logger.error("url匹配失败！" + url);
			return null;
		}
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		if (params != null && params.size() > 0) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (String key : params.keySet()) {
				parameters.add(new BasicNameValuePair(key, params.get(key)));
			}
			// 构造一个form表单式的实体
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, encoding);
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(formEntity);
		}

		return retryHttpPostRequest(encoding, httpPost, 3);
	}

	public String post(String url, String encoding) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		return post(url, map, encoding);
	}

	public String post(String url, String params, String encoding) throws Exception {
		if (!checkUrl(url)) {
			logger.error("url匹配失败！" + url);
			return null;
		}
		HttpPost httpPost = new HttpPost(url);
		StringEntity strEntity = new StringEntity(params, encoding);
		strEntity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(strEntity);
		return retryHttpPostRequest(encoding, httpPost, 3);
	}

	public <T> String postXml(String url, T obj, String encoding) throws Exception {
		if (!checkUrl(url)) {
			logger.error("url匹配失败！" + url);
			return null;
		}
		HttpPost httpPost = new HttpPost(url);
		// 解决XStream对出现双下划线的bug
		XStream xStreamForRequestPostData = new XStream(new DomDriver(encoding, new XmlFriendlyNameCoder("-_", "_")));
		xStreamForRequestPostData.autodetectAnnotations(true);
		// 将要提交给API的数据对象转换成XML格式数据Post给API
		String postDataXML = xStreamForRequestPostData.toXML(obj);

		logger.debug("API，POST过去的数据是：");
		logger.debug(postDataXML);
		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(postDataXML, encoding);
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);

		return retryHttpPostRequest(encoding, httpPost, 3);
	}

	@Override
	public <T> String postXml(CloseableHttpClient httpClient, String url, T obj, String encoding) throws Exception {
		if (!checkUrl(url)) {
			logger.error("url匹配失败！" + url);
			return null;
		}
		HttpPost httpPost = new HttpPost(url);
		// 解决XStream对出现双下划线的bug
		XStream xStreamForRequestPostData = new XStream(new DomDriver(encoding, new XmlFriendlyNameCoder("-_", "_")));
		xStreamForRequestPostData.autodetectAnnotations(true);
		// 将要提交给API的数据对象转换成XML格式数据Post给API
		String postDataXML = xStreamForRequestPostData.toXML(obj);

		logger.debug("API，POST过去的数据是：");
		logger.debug(postDataXML);
		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(postDataXML, encoding);
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);

		return executeHttpRequest(httpPost, encoding, httpClient);
	}

	/**
	 * 提交json数据
	 * 
	 * @param url
	 * @param json
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postJson(String url, String json, String encoding) throws Exception {
		if (!checkUrl(url)) {
			logger.error("url匹配失败！" + url);
			return null;
		}
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		if (json != null) {
			// 构造一个form表单式的实体
			StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(stringEntity);
		}
		return retryHttpPostRequest(encoding, httpPost, 3);
	}


	private String retryHttpPostRequest(String encoding, HttpPost httpPost, int retryTime) throws Exception {
		String reply = null;
		try {
			reply = executeHttpRequest(httpPost, encoding);
		} catch (Exception e) {
			if (retryTime-- > 0) retryHttpPostRequest(encoding, httpPost, retryTime);
			else throw e;
		}
		return reply;
	}

	public String postJson(String url, String json, Map<String, String> headers, String encoding) throws Exception {
		if (!checkUrl(url)) {
			logger.error("url匹配失败！" + url);
			return null;
		}
		// 创建http POST请求
		HttpPost httpPost = new HttpPost(url);
		if (json != null) {
			// 构造一个form表单式的实体
			StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
			// 将请求实体设置到httpPost对象中
			httpPost.setEntity(stringEntity);
		}
		
		if(headers != null && headers.size() > 0){
			for(String key : headers.keySet()){
				httpPost.addHeader(key, headers.get(key));
			}
		}

		return retryHttpPostRequest(encoding, httpPost, 3);
	}

	private boolean checkUrl(String url) {
		if (url.startsWith("https://") || url.startsWith("http://")) {
			return true;
		}

		return false;
	}

	public static class HttpRetryHandler implements HttpRequestRetryHandler {

		public HttpRetryHandler() {

		}

		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			if (executionCount >= 1) {// 如果已经重试了5次，就放弃
				return false;
			}
			if (exception instanceof InterruptedIOException) {// 超时
				return false;
			}
			if (exception instanceof UnknownHostException) {// 目标服务器不可达
				return false;
			}
			if (exception instanceof SSLException) {// ssl握手异常
				return false;
			}

			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			// 如果请求是幂等的，就再次尝试
			if (!(request instanceof HttpEntityEnclosingRequest)) {
				return true;
			}
			return false;
		}

	}

	public static class MyConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

		public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
			HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			while (it.hasNext()) {
				HeaderElement he = it.nextElement();
				String param = he.getName();
				String value = he.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					try {
						return Long.parseLong(value) * 1000;
					} catch (NumberFormatException ignore) {
					}
				}
			}
			return 60 * 1000;
		}

	}

	// 绕过https安全认证的post请求-》》 暂时用来测试苹果支付 （改下代码设置一下不验https证书，测试环境不验证书才能成功，生产会验证书。）
	// 生成的话，使用post(String url,Map<String,String> map,String encoding) 方法就可以了
	public String send(String url, Map<String, String> map, String encoding) throws Exception {
		String body = "";
		CloseableHttpResponse response = null;
		HttpPost httpPost = null;
		// 采用绕过验证的方式处理https请求
		SSLContext sslcontext = createIgnoreVerifySSL();
		try {
			// 设置协议http和https对应的处理socket链接工厂的对象
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(sslcontext)).build();
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
					socketFactoryRegistry);
			HttpClients.custom().setConnectionManager(connManager);

			// 创建自定义的httpclient对象
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();

			// 创建post方式请求对象
			httpPost = new HttpPost(url);

			// 装填参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (map != null) {
				for (Entry<String, String> entry : map.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
			}
			// 设置参数到请求对象中
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
			// 设置header信息
			// 指定报文头【Content-type】、【User-Agent】
			// httpPost.setHeader("Content-type",
			// "application/x-www-form-urlencoded");
			// httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE
			// 5.0; Windows NT; DigExt)");

			// 执行请求操作，并拿到结果（同步阻塞）

			response = client.execute(httpPost);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 按指定编码转换结果实体为String类型
				body = EntityUtils.toString(entity, encoding);
			}
			EntityUtils.consume(entity);
			abortRequest(httpPost);

		} catch (Exception ex) {
			abortRequest(httpPost);
			throw new Exception("http client " + httpPost.getMethod() + "请求失败！", ex);
		} finally {
			// 释放链接
			response.close();
		}
		return body;
	}

	/**
	 * 绕过验证
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}
}
