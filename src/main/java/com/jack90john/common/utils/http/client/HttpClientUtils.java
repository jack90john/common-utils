package com.jack90john.common.utils.http.client;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @apiNote 网络请求工具（封装apache-httpClient）
 * @author jack
 * @version 1.0.0
 * @since 2.0.0.RELEASE
 */
public class HttpClientUtils {

    private HttpClientUtils() {
    }

    /**
     * 发送无参的Get请求
     *
     * @param url 请求URL
     * @return {@link HttpClientResult} 请求结果
     * @throws IOException IOException
     * @since 2.0.0.RELEASE
     */
    public static HttpClientResult doGet(String url) throws IOException {
        return doGetWithHeader(url, null);
    }

    /**
     * 发送带参的Get请求
     *
     * @param url   请求URL
     * @param param 参数Map
     * @return {@link HttpClientResult} 请求结果
     * @throws IOException IOException
     * @since 2.0.0.RELEASE
     */
    public static HttpClientResult doGet(String url, Map<String, String> param) throws IOException {
        return doGetWithHeader(combineUrlAndParam(url, param), null);
    }

    /**
     * 发送带参数和头信息的Get请求
     *
     * @param url     请求URL
     * @param param   参数Map
     * @param headers 头信息Map
     * @return {@link HttpClientResult} 请求结果
     * @throws IOException IOException
     * @since 2.0.0.RELEASE
     */
    public static HttpClientResult doGetWithHeader(String url, Map<String, String> param, Map<String, String> headers) throws IOException {
        return doGetWithHeader(combineUrlAndParam(url, param), headers);
    }

    /**
     * 发送无参数但是有头信息的Get请求
     *
     * @param url     请求URL
     * @param headers 头信息Map
     * @return {@link HttpClientResult} 请求结果
     * @throws IOException IOException
     * @since 2.0.0.RELEASE
     */
    public static HttpClientResult doGetWithHeader(String url, Map<String, String> headers) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = buildClient();
        CloseableHttpResponse response = null;
        HttpClientResult result = new HttpClientResult();
        try {
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(url);
            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach(httpGet::addHeader);
            }
            // 执行请求
            response = httpClient.execute(httpGet);
            String data = EntityUtils.toString(response.getEntity(), "UTF-8");
            result.setStatus(response.getStatusLine().getStatusCode());
            result.setResult(data);
            result.setHeaders(response.getAllHeaders());
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return result;
    }

    /**
     * 发送无参的Post请求
     *
     * @param url 请求URL
     * @return {@link HttpClientResult} 请求结果
     * @throws IOException IOException
     * @since 2.0.0.RELEASE
     */
    public static HttpClientResult doPost(String url) throws IOException {
        return doPostWithHeader(url, null, null);
    }

    /**
     * 发送带参（Json格式）的Post请求
     *
     * @param url       请求URL
     * @param jsonParam Json参数
     * @return {@link HttpClientResult} 请求结果
     * @throws IOException IOException
     * @since 2.0.0.RELEASE
     */
    public static HttpClientResult doPost(String url, String jsonParam) throws IOException {
        return doPostWithHeader(url, jsonParam, null);
    }

    /**
     * 发送无参数但是有头信息的Post请求
     *
     * @param url     请求URL
     * @param headers 头信息Map
     * @return {@link HttpClientResult} 请求结果
     * @throws IOException IOException
     * @since 2.0.0.RELEASE
     */
    public static HttpClientResult doPost(String url, Map<String, String> headers) throws IOException {
        return doPostWithHeader(url, null, headers);
    }

    /**
     * 发送带参数（Json格式）和头信息的Post请求
     *
     * @param url       请求URL
     * @param jsonParam Json参数
     * @param headers   头信息Map
     * @return {@link HttpClientResult} 请求结果
     * @throws IOException IOException
     * @since 2.0.0.RELEASE
     */
    public static HttpClientResult doPostWithHeader(String url, String jsonParam, Map<String, String> headers) throws IOException {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = buildClient();
        CloseableHttpResponse response = null;
        HttpClientResult httpClientResult = new HttpClientResult();
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.expectContinue();
            // 创建请求内容
            if (StringUtils.isNotEmpty(jsonParam)) {
                StringEntity entity = new StringEntity(jsonParam, ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
            }
            if (MapUtils.isNotEmpty(headers)) {
                headers.forEach(httpPost::addHeader);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            String data = EntityUtils.toString(response.getEntity(), "UTF-8");
            httpClientResult.setResult(data);
            httpClientResult.setHeaders(response.getAllHeaders());
            httpClientResult.setStatus(response.getStatusLine().getStatusCode());
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return httpClientResult;
    }

    /**
     * 建立httpClient
     * @return CloseableHttpClient
     * @since 2.0.0.RELEASE
     */
    private static CloseableHttpClient buildClient() {
        SSLContextBuilder builder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);
        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(cm)
                .build();
    }

    /**
     * 构建带参数GET请求的Url
     * @param uri uri
     * @param param 参数
     * @return 完整URL
     * @since 2.0.0.RELEASE
     */
    private static String combineUrlAndParam(String uri, Map<String, String> param) {
        StringBuilder stb = new StringBuilder(uri);
        stb.append("?");
        param.forEach((s, s2) -> stb.append(s).append("=").append(s2).append("&"));
        stb.deleteCharAt(stb.lastIndexOf("&"));
        return stb.toString();
    }

}
