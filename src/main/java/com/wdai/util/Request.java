/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;

import us.codecraft.webmagic.utils.HttpConstant;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * @author reus
 * @version $Id: Request.java, v 0.1 2017-03-31 reus Exp $
 */
public class Request{

    public Request(){

    }

    public Request(String url){
        this.url = url;
        hostname = UrlUtils.getDomain(url);
    }

    /** 请求url */
    private String url;

    /** 请求方法 */
    private String method = HttpConstant.Method.GET;

    /** 请求额外信息 */
    private Map<String, Object> extras;

    /** 请求优先级 */
    private long priority;

    /** 请求头 */
    private Map<String,String> headers;

    /** 代理服务器 */
    private HttpHost httpHost;

    /** 代理是否需要认证*/
    private boolean isAuth;

    /** 当代理需要认证时此值不能为空 */
    private UsernamePasswordCredentials usernamePasswordCredentials;

    /** 超时时间 */
    private int timeOut = 60000;

    /** 网站域名 */
    private String hostname;

    /** http请求失败重试次数 */
    private int retryTimes = 0;

    /** http请求失败是否需要重试 */
    private boolean needRetry = true;

    /** 请求体 */
    private Map<String,Object> body;

    /** post请求时,Content-Type的值 */
    private int type = 1;

    /** 是否需要去重 */
    private boolean needDuplicate = true;

    /** 302是否自动跳转 */
    private boolean redirectsEnabled = true;

    /** 添加单个请求头 */
    public Request addHeader(String name, String vaule){
        if(headers == null)
            headers = new HashMap<>();
        if(StringUtils.isNotBlank(name))
            headers.put(name,vaule);
        return this;
    }

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public Request putExtra(String key, Object value) {
        if (extras == null) {
            extras = new HashMap<String, Object>();
        }
        extras.put(key, value);
        return this;
    }

    /**
     * Getter method for property <tt>headers</tt>.
     *
     * @return property value of headers
     */
    public Map<String, String> getHeaders() {
        if(headers != null)
            return headers;
        else
            return new HashMap<String, String>();
    }

    /**
     * Setter method for property <tt>headers</tt>.
     *
     * @param headers value to be assigned to property headers
     */
    public Request setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Getter method for property <tt>httpHost</tt>.
     *
     * @return property value of httpHost
     */
    public HttpHost getHttpHost() {
        return httpHost;
    }

    /**
     * Setter method for property <tt>httpHost</tt>.
     *
     * @param httpHost value to be assigned to property httpHost
     */
    public Request setHttpHost(HttpHost httpHost) {
        this.httpHost = httpHost;
        return this;
    }

    /**
     * Getter method for property <tt>isAuth</tt>.
     *
     * @return property value of isAuth
     */
    public boolean getIsAuth() {
        return isAuth;
    }

    /**
     * Setter method for property <tt>isAuth</tt>.
     *
     * @param isAuth value to be assigned to property isAuth
     */
    public void setIsAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    /**
     * Getter method for property <tt>usernamePasswordCredentials</tt>.
     *
     * @return property value of usernamePasswordCredentials
     */
    public UsernamePasswordCredentials getUsernamePasswordCredentials() {
        return usernamePasswordCredentials;
    }

    /**
     * Setter method for property <tt>usernamePasswordCredentials</tt>.
     *
     * @param usernamePasswordCredentials value to be assigned to property usernamePasswordCredentials
     */
    public void setUsernamePasswordCredentials(UsernamePasswordCredentials usernamePasswordCredentials) {
        this.usernamePasswordCredentials = usernamePasswordCredentials;
    }

    /**
     * Getter method for property <tt>timeOut</tt>.
     *
     * @return property value of timeOut
     */
    public int getTimeOut() {
        return timeOut;
    }

    /**
     * Setter method for property <tt>timeOut</tt>.
     *
     * @param timeOut value to be assigned to property timeOut
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * Getter method for property <tt>hostname</tt>.
     *
     * @return property value of hostname
     */
    public String getHostname() {
        if(StringUtils.isBlank(hostname))
            hostname = UrlUtils.getDomain(url);
        return hostname;
    }

    /**
     * Setter method for property <tt>hostname</tt>.
     *
     * @param hostname value to be assigned to property hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Getter method for property <tt>retryTimes</tt>.
     *
     * @return property value of retryTimes
     */
    public int getRetryTimes() {
        return retryTimes;
    }

    /**
     * Setter method for property <tt>retryTimes</tt>.
     *
     * @param retryTimes value to be assigned to property retryTimes
     */
    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    /**
     * Getter method for property <tt>needRetry</tt>.
     *
     * @return property value of needRetry
     */
    public boolean isNeedRetry() {
        return needRetry;
    }

    /**
     * Setter method for property <tt>needRetry</tt>.
     *
     * @param needRetry value to be assigned to property needRetry
     */
    public void setNeedRetry(boolean needRetry) {
        this.needRetry = needRetry;
    }

    /**
     * Getter method for property <tt>body</tt>.
     *
     * @return property value of body
     */
    public Map<String, Object> getBody() {
        return body;
    }

    /**
     * Setter method for property <tt>body</tt>.
     *
     * @param body value to be assigned to property body
     */
    public Request setBody(Map<String, Object> body) {
        this.body = body;
        return this;
    }

    /**
     * Getter method for property <tt>url</tt>.
     *
     * @return property value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter method for property <tt>url</tt>.
     *
     * @param url value to be assigned to property url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter method for property <tt>method</tt>.
     *
     * @return property value of method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Setter method for property <tt>method</tt>.
     *
     * @param method value to be assigned to property method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Getter method for property <tt>extras</tt>.
     *
     * @return property value of extras
     */
    public Map<String, Object> getExtras() {
        return extras;
    }

    /**
     * Setter method for property <tt>extras</tt>.
     *
     * @param extras value to be assigned to property extras
     */
    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    /**
     * Getter method for property <tt>priority</tt>.
     *
     * @return property value of priority
     */
    public long getPriority() {
        return priority;
    }

    /**
     * Setter method for property <tt>priority</tt>.
     *
     * @param priority value to be assigned to property priority
     */
    public Request setPriority(long priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public int getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     *
     * @param type value to be assigned to property type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Getter method for property <tt>needDuplicate</tt>.
     *
     * @return property value of needDuplicate
     */
    public boolean isNeedDuplicate() {
        return needDuplicate;
    }

    /**
     * Setter method for property <tt>needDuplicate</tt>.
     *
     * @param needDuplicate value to be assigned to property needDuplicate
     */
    public Request setNeedDuplicate(boolean needDuplicate) {
        this.needDuplicate = needDuplicate;
        return this;
    }

    /**
     * Getter method for property <tt>redirectsEnabled</tt>.
     *
     * @return property value of needDuplicate
     */
    public boolean isRedirectsEnabled() {
        return redirectsEnabled;
    }

    /**
     * Setter method for property <tt>redirectsEnabled</tt>.
     *
     * @param redirectsEnabled value to be assigned to property needDuplicate
     */
    public void setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
    }
}