/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.HttpConstant;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * @author reus
 * @version $Id: ImproveDownloader.java, v 0.1 2017-03-06 reus Exp $
 */
public class ImproveDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private HttpClientGeneratorMore httpClientGenerator = new HttpClientGeneratorMore();

    private CloseableHttpClient getHttpClient(Request request) {
        CloseableHttpClient httpClient = httpClients.get(request.getHostname());
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(request);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(request);
                    httpClients.put(request.getHostname(), httpClient);
                }
            }
        }
        return httpClient;
    }

    /**
     * 下载资源,并对其进行封装
     *
     * @param request
     * @return
     */
    public Page download(Request request) {
        Map<String, String> headers = request.getHeaders();
        logger.info("downloading page {}", request.getUrl());
        CloseableHttpResponse httpResponse = null;
        int statusCode;
        try {
            HttpHost proxyHost = request.getHttpHost();
            HttpUriRequest httpUriRequest = getHttpUriRequest(request, headers, proxyHost);
            httpResponse = getHttpClient(request).execute(httpUriRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            Page page = handleResponse(request, httpResponse);
            page.setStatusCode(statusCode);
            return page;
        } catch (IOException e) {
            logger.warn("download page {} error", request.getUrl(), e);
            return null;
        } finally {
            try {
                if (httpResponse != null) {
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
                logger.warn("close response fail", e);
            }
        }
    }

    /**
     * 将资源转换成字符串
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    protected String getContent(HttpResponse httpResponse, Page page) throws IOException {
        String content;
        byte[] contentBytes;
        Header header = httpResponse.getFirstHeader("Content-Type");
        String contentType = "";
        if (header != null)
            contentType = header.getValue();
        if (StringUtils.isNotBlank(contentType) && contentType.contains("image")) {
            String info = EntityUtils.toString(httpResponse.getEntity(), Charsets.ISO_8859_1);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(info.getBytes(Charsets.ISO_8859_1));
            contentBytes = IOUtils.toByteArray(inputStream);
        } else {
            contentBytes = EntityUtils.toByteArray(httpResponse.getEntity());
        }
        page.setBytes(contentBytes);
        page.setHeaders(httpResponse.getAllHeaders());

        String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
        if (StringUtils.isNotBlank(htmlCharset)) {
            page.setCharset(htmlCharset);
            if (StringUtils.isNotBlank(contentType) && contentType.contains("image")) {
                content = Base64.encodeBase64String(contentBytes);
            } else {
                content = new String(contentBytes, htmlCharset);
            }
            return content;
        } else {
            logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
            if (StringUtils.isNotBlank(contentType) && contentType.contains("image")) {
                content = Base64.encodeBase64String(contentBytes);
            } else {
                content = new String(contentBytes);
            }
            page.setCharset(Consts.UTF_8.displayName());
            return content;
        }
    }

    /**
     * 组建请求
     *
     * @param request
     * @param headers
     * @param proxy
     * @return
     */
    protected HttpUriRequest getHttpUriRequest(Request request, Map<String, String> headers, HttpHost proxy) throws UnsupportedEncodingException {
        String url = request.getUrl();
        url = url.replaceAll(" ", "%20").replaceAll("\\[",URLEncoder.encode("[",Consts.UTF_8.displayName()));
        url = url.replaceAll("]",URLEncoder.encode("]",Consts.UTF_8.displayName()));
        url = url.replaceAll("\\{",URLEncoder.encode("{",Consts.UTF_8.displayName()));
        url = url.replaceAll("}",URLEncoder.encode("}",Consts.UTF_8.displayName()));
        url = url.replaceAll("`",URLEncoder.encode("`",Consts.UTF_8.displayName()));
        url = url.replaceAll("\\|",URLEncoder.encode("|",Consts.UTF_8.displayName()));
        request.setUrl(url);
        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(url);
        if (headers != null) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(request.getTimeOut())
                .setSocketTimeout(request.getTimeOut())
                .setConnectTimeout(request.getTimeOut())
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setRedirectsEnabled(request.isRedirectsEnabled());
        if (proxy != null)
            requestConfigBuilder.setProxy(proxy);
        requestBuilder.setConfig(requestConfigBuilder.build());
        return requestBuilder.build();
    }

    /**
     * 对请求方式进行处理
     *
     * @param request
     * @return
     */
    protected RequestBuilder selectRequestMethod(Request request) {
        String method = request.getMethod();
        RequestBuilder requestBuilder;
        if (method == null || method.equalsIgnoreCase(HttpConstant.Method.GET)) {
            requestBuilder = RequestBuilder.get();
            if (request.getHeaders().containsKey(HttpHeaders.CONTENT_TYPE))
                requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, "text/html");
            //default get
            return requestBuilder;
        } else if (method.equalsIgnoreCase(HttpConstant.Method.POST)) {
            requestBuilder = RequestBuilder.post();
            Map<String, Object> body = request.getBody();
            switch (request.getType()) {
                case 1:
                    requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
                    if (body != null && body.size() > 0) {
                        for (Map.Entry<String, Object> entry : body.entrySet()) {
                            requestBuilder.addParameter(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                        }
                    }
                    break;
                case 2:
                    requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    JSONObject jsonObject = new JSONObject();
                    if (body != null && body.size() > 0) {
                        for (Map.Entry<String, Object> entry : body.entrySet()) {
                            jsonObject.put(entry.getKey(), entry.getValue().toString());
                        }
                    }
                    StringEntity entity = new StringEntity(jsonObject.toString(), Consts.UTF_8);
                    entity.setContentEncoding(Consts.UTF_8.displayName());
                    entity.setContentType("application/json");
                    requestBuilder.setEntity(entity);
                    break;
                case 3:
                    requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
                    StringEntity xmlEntity = new StringEntity(body.get("xmlString").toString(), Consts.UTF_8);
                    xmlEntity.setContentEncoding(Consts.UTF_8.displayName());
                    xmlEntity.setContentType("application/xml");
                    requestBuilder.setEntity(xmlEntity);
            }
            return requestBuilder;
        } else if (method.equalsIgnoreCase(HttpConstant.Method.HEAD)) {
            return RequestBuilder.head();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.PUT)) {
            return RequestBuilder.put();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.DELETE)) {
            return RequestBuilder.delete();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.TRACE)) {
            return RequestBuilder.trace();
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    /**
     * 处理网页内容
     *
     * @param request
     * @param httpResponse
     * @return
     * @throws IOException
     */
    protected Page handleResponse(Request request, HttpResponse httpResponse) throws IOException {
        Page page = new Page();
        String content = getContent(httpResponse, page);
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        return page;
    }

    /**
     * 获取页面的编码
     *
     * @param httpResponse
     * @param contentBytes
     * @return
     * @throws IOException
     */
    protected String getHtmlCharset(HttpResponse httpResponse, byte[] contentBytes) throws IOException {
        String charset = "";
        // charset
        // 1、encoding in http header Content-Type
        if(httpResponse.getEntity().getContentType() != null) {
            String value = httpResponse.getEntity().getContentType().getValue();
            charset = UrlUtils.getCharset(value);
            if (StringUtils.isNotBlank(charset)) {
                if("gb2312".equalsIgnoreCase(charset))
                    charset = "gbk";
                logger.info("Auto get charset: {}", charset);
                return charset;
            }
        }
        // use default charset to decode first time
        Charset defaultCharset = Charset.defaultCharset();
        String content = new String(contentBytes, defaultCharset.name());
        final String meta = "meta";
        final String charsetStr = "charset";
        // 2、charset in meta
        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select(meta);
            for (Element link : links) {
                // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                String metaContent = link.attr("content");
                String metaCharset = link.attr(charsetStr);
                if (metaContent.indexOf("charset") != -1) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    charset = metaContent.split("=")[1];
                    break;
                }
                // 2.2、html5 <meta charset="UTF-8" />
                else if (StringUtils.isNotEmpty(metaCharset)) {
                    charset = metaCharset;
                    break;
                }
            }
        }
        logger.info("Auto get charset: {}", charset);
        // 3、todo use tools as cpdetector for content decode
        if("gb2312".equalsIgnoreCase(charset))
            charset = "gbk";
        return charset;
    }

}