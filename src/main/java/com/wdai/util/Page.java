/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * @author reus
 * @version $Id: Page.java, v 0.1 2017-04-01 reus Exp $
 */
public class Page {

    /** 请求内容 */
    private Request request;

    /** html网页 */
    private Html html;

    /** 网页源代码 */
    private String rawText;

    /** 请求响应状态码 */
    private int statusCode;

    /** 请求内容字节码 */
    private byte[] bytes;

    /** 网页编码 */
    private String charset;

    /** 响应头 */
    private Header[] headers;

    /** 请求地址 */
    private Selectable url;

    /** 页面中url */
    private List<Request> targetRequests = new ArrayList<Request>();

    /** 页面要存储的数据 */
    private ResultItems resultItems = new ResultItems();

    /** 图片存储的bucket地址 */
    private String bucket;

    /** 图片存储的额外信息 */
    private String prefix;

    /**
     * 添加一组请求
     * @param requests
     */
    public void addTargetRequests(List<Request> requests){
        synchronized (targetRequests){
            for(Request request:requests){
                targetRequests.add(request);
            }
        }
    }

    /**
     * 添加单个请求
     * @param request
     */
    public void addTargetRequest(Request request){
        synchronized (targetRequests){
            targetRequests.add(request);
        }
    }

    /**
     * 添加数据
     * @param key
     * @param field
     */
    public void putField(String key, Object field) {
        synchronized (resultItems){
            resultItems.put(key, field);
        }
    }

    /**
     * Getter method for property <tt>targetRequests</tt>.
     *
     * @return property value of targetRequests
     */
    public List<Request> getTargetRequests() {
        return targetRequests;
    }

    /**
     * Getter method for property <tt>request</tt>.
     *
     * @return property value of request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Setter method for property <tt>request</tt>.
     *
     * @param request value to be assigned to property request
     */
    public void setRequest(Request request) {
        this.request = request;
        this.resultItems.setRequest(request);
    }

    /**
     * Getter method for property <tt>html</tt>.
     *
     * @return property value of html
     */
    public Html getHtml() {
        if (html == null) {
            html = new Html(UrlUtils.fixAllRelativeHrefs(rawText, request.getUrl()));
        }
        return html;
    }

    /**
     * Setter method for property <tt>html</tt>.
     *
     * @param html value to be assigned to property html
     */
    public void setHtml(Html html) {
        this.html = html;
    }

    /**
     * Getter method for property <tt>rawText</tt>.
     *
     * @return property value of rawText
     */
    public String getRawText() {
        return rawText;
    }

    /**
     * Setter method for property <tt>rawText</tt>.
     *
     * @param rawText value to be assigned to property rawText
     */
    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    /**
     * Getter method for property <tt>statusCode</tt>.
     *
     * @return property value of statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Setter method for property <tt>statusCode</tt>.
     *
     * @param statusCode value to be assigned to property statusCode
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Getter method for property <tt>bytes</tt>.
     *
     * @return property value of bytes
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Setter method for property <tt>bytes</tt>.
     *
     * @param bytes value to be assigned to property bytes
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Getter method for property <tt>charset</tt>.
     *
     * @return property value of charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Setter method for property <tt>charset</tt>.
     *
     * @param charset value to be assigned to property charset
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * Getter method for property <tt>headers</tt>.
     *
     * @return property value of headers
     */
    public Header[] getHeaders() {
        return headers;
    }

    /**
     * Setter method for property <tt>headers</tt>.
     *
     * @param headers value to be assigned to property headers
     */
    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    /**
     * Getter method for property <tt>url</tt>.
     *
     * @return property value of url
     */
    public Selectable getUrl() {
        return url;
    }

    /**
     * Setter method for property <tt>url</tt>.
     *
     * @param url value to be assigned to property url
     */
    public void setUrl(Selectable url) {
        this.url = url;
    }

    /**
     * Getter method for property <tt>resultItems</tt>.
     *
     * @return property value of resultItems
     */
    public ResultItems getResultItems() {
        return resultItems;
    }

    /**
     * Getter method for property <tt>bucket</tt>.
     *
     * @return property value of bucket
     */
    public String getBucket() {
        return bucket;
    }

    /**
     * Setter method for property <tt>bucket</tt>.
     *
     * @param bucket value to be assigned to property bucket
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * Getter method for property <tt>prefix</tt>.
     *
     * @return property value of prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Setter method for property <tt>prefix</tt>.
     *
     * @param prefix value to be assigned to property prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}