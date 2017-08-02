/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import us.codecraft.webmagic.downloader.CustomRedirectStrategy;

/**
 * @author reus
 * @version $Id: HttpClientGeneratorMore.java, v 0.1 2017-03-10 reus Exp $
 */
public class HttpClientGeneratorMore{

    private Logger logger = LoggerFactory.getLogger(getClass());

    private PoolingHttpClientConnectionManager connectionManager;

    public HttpClientGeneratorMore() {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnectionSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);
        connectionManager.setMaxTotal(20);
    }

    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL()); // 优先绕过安全证书
        } catch (KeyManagementException e) {
            logger.error("ssl connection fail", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("ssl connection fail", e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        SSLContext sc = SSLContext.getInstance("SSLv3");
        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

    public HttpClientGeneratorMore setPoolSize(int poolSize) {
        connectionManager.setMaxTotal(poolSize);
        return this;
    }

    public CloseableHttpClient getClient(Request request) {
        return generateClient(request);
    }

    private CloseableHttpClient generateClient(Request request) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager);
        httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

            public void process(
                    final HttpRequest request,
                    final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader(HttpHeaders.ACCEPT_ENCODING)) {
                    request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, sdch");
                }
                if(!request.containsHeader(HttpHeaders.USER_AGENT)){
                    request.addHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
                }
                if(!request.containsHeader(HttpHeaders.ACCEPT)){
                    request.addHeader(HttpHeaders.ACCEPT,"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                }
            }
        });
        //解决post/redirect/post 302跳转问题
        httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());

        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(request.getTimeOut()).setSoTimeout(request.getTimeOut()).setSoKeepAlive(true).setTcpNoDelay(true).build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultSocketConfig(socketConfig);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(request.getRetryTimes(), request.isNeedRetry()));
        return httpClientBuilder.build();
    }
}