package com.yonyou.esn.yycollege.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;


/**
 * 通讯https工具类
 * @author lizhgb
 * @date  2015-9-6 
 *
 */
public  class HttpClientUtil {
	
	private static final Logger LOGGER = Logger.getLogger(HttpClientUtil.class);
	
	private static CloseableHttpClient httpclient = createSSLClientDefault();

	private static final String DEFAULT_CHARSET_UTF8 = "UTF-8";
	private static final String DEFAULT_CONTENT_TYPE_JSON = "application/json";
	private static final int MAX_TIMEOUT = 30000;
	private static final int MAX_RETRY_TIMES = 5;
	private static final int MAX_THREAD_TOTAL = 50;


	/**
	 * 发送https post请求
	 * @param action
	 * @param bodyParam
	 * @return
	 * @throws Exception
	 * @author   lizhgb
	 * @Date   2015-9-10 上午11:12:55
	 */
	public static  String httpsPost(String action, Object bodyParam) throws Exception{
		return httpsPost(action, null, bodyParam, null, null);
	}
	
	
	/**
	 * 发送https post请求
	 * @param action
	 * @param bodyParam
	 * @return
	 * @throws Exception
	 * @author   lizhgb
	 * @Date   2015-9-10 上午11:12:55
	 */
	public static String httpsPost(String action, Map<String, String> headerParam, Object bodyParam) throws Exception{
		return httpsPost(action, headerParam, bodyParam, null, null);
	}
	
	
	
	/**
	 * 发送https post请求
	 * 
	 * @param action
	 * @return
	 * @author lizhgb
	 * @throws Exception 
	 * @throws UnsupportedEncodingException
	 * @Date 2015-9-6 下午1:32:06
	 */
	public static String httpsPost(String action, Map<String, String> headerParam, Object bodyParam, String contentType, String charSet) throws Exception{
		
		
		String content_type = contentType;
		if (content_type == null || "".equals(content_type)) content_type = DEFAULT_CONTENT_TYPE_JSON;
		
		String char_set = charSet;
		if (char_set == null || "".equals(char_set)) char_set = DEFAULT_CHARSET_UTF8;		
		
		String url = action;
		LOGGER.info("Post请求地址：" + url);
		
		HttpPost httpPost = new HttpPost(url);		
		
		//header参数
		if (headerParam != null && headerParam.size() >0){
			LOGGER.info("Post请求Header：" + JSON.toJSONString(headerParam));
			for (String key : headerParam.keySet()){
				httpPost.addHeader(key, headerParam.get(key));
			}
		}			
		
		//entity数据
		if (bodyParam != null ) {
			LOGGER.info("Post请求Body：" + JSON.toJSONString(bodyParam));
			StringEntity entity = new StringEntity(JSON.toJSONString(bodyParam),char_set);	
			entity.setContentEncoding(char_set);
			entity.setContentType(content_type);
			httpPost.setEntity(entity);		
		}
		
		String resultStr = "";	
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
				httpPost.releaseConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		LOGGER.info("Post请求返回：" + resultStr);
		return resultStr;
	}



	/**
	 * 发送https get请求
	 * @param action
	 * @return
	 * @throws Exception
	 * @author   lizhgb
	 * @Date   2015-9-7 上午9:06:57
	 */
	public static String httpsGet(String action) throws Exception{
		

		String url =  action;
		LOGGER.info("Get请求地址：" + url);
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");		

		CloseableHttpResponse response = null;
		String resultStr = "";
		try {
			response = httpclient.execute(httpGet);
			resultStr = EntityUtils.toString(response.getEntity(),"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
				httpGet.releaseConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		LOGGER.info("Get请求返回：" + resultStr);
		return resultStr;
	}
	

	
	/**
	 * 发送https get请求
	 * @param action
	 * @return
	 * @throws Exception
	 * @author   lizhgb
	 * @Date   2015-9-7 上午9:06:57
	 */
	public static String httpsGet(String action, Map<String,String> params) throws Exception{
		
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setPath(action);
		if (params != null){
			for (String key: params.keySet()){
				uriBuilder.setParameter(key, params.get(key));
			}			
		}			
		return httpsGet(uriBuilder.build().toString());
	}
	

	/**
	 * 创建httpclient
	 * 
	 * @return
	 * @author lizhgb
	 * @Date 2015-9-6 下午1:20:21
	 */
	private static synchronized CloseableHttpClient createSSLClientDefault() {
		CloseableHttpClient httpclient = null;
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, 
					new TrustStrategy() {
						public boolean isTrusted(
								java.security.cert.X509Certificate[] chain,
								String authType)
								throws java.security.cert.CertificateException {							
							return true;
						}
					}).build();
			

		   SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
		   ConnectionSocketFactory psf = PlainConnectionSocketFactory.getSocketFactory();  
		   
		   Registry<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create()
		                .register("https", sslsf)
		                 .register("http", psf)
		                .build();

			RequestConfig config = RequestConfig.custom()
					  .setSocketTimeout(MAX_TIMEOUT)
					  .setConnectTimeout(MAX_TIMEOUT)
					  .setConnectionRequestTimeout(MAX_TIMEOUT)
					  .setStaleConnectionCheckEnabled(true)
					  .build();
			//超时重试处理			
			HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(MAX_RETRY_TIMES, true);
			
			//连接管理池
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registryBuilder);
			cm.setMaxTotal(MAX_THREAD_TOTAL);
			cm.setDefaultMaxPerRoute(MAX_THREAD_TOTAL);
			
			httpclient = HttpClients.custom().setConnectionManager(cm).setSSLSocketFactory(sslsf).setDefaultRequestConfig(config).setRetryHandler(retryHandler).build();
		
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return httpclient;
	}
}
