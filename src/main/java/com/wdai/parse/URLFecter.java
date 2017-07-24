/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.parse;

import com.wdai.http.HttpUtils;
import com.wdai.model.IPAdress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import static java.lang.System.out;

/**
 * @author reus
 * @version $Id: URLFecter.java, v 0.1 2017-07-20 reus Exp $
 */
public class URLFecter {
    //使用代理进行爬取
    public static List<IPAdress> urlParse(String url, String ip, String port,
                                          List<IPAdress> ipMessages) throws ClassNotFoundException,
                                                                     IOException {
        //调用一个类使其返回html源码
        String html = HttpUtils.getHtml(url, ip, port);

        if (html != null) {
            //将html解析成DOM结构
            Document document = Jsoup.parse(html);

            //提取所需要的数据
            Elements trs = document.select("table[id=ip_list]").select("tbody").select("tr");
            parseTable(trs, ipMessages);
        } else {
            out.println(ip + ": " + port + " 代理不可用");
        }

        return ipMessages;
    }

    //使用本机IP爬取xici代理网站的第一页
    public static List<IPAdress> urlParse(String url, List<IPAdress> list) throws IOException,
                                                                           ClassNotFoundException {
        String html = HttpUtils.getHtml(url);

        //将html解析成DOM结构
        Document document = Jsoup.parse(html);

        //提取所需要的数据
        Elements trs = document.select("table[id=ip_list]").select("tbody").select("tr");

        parseTable(trs, list);

        return list;
    }

    public static void parseTable(Elements trs, List<IPAdress> list) {
        for (int i = 1; i < trs.size(); i++) {
            IPAdress ipMessage = new IPAdress();
            String ipAddress = trs.get(i).select("td").get(1).text();
            String ipPort = trs.get(i).select("td").get(2).text();
            String serverAddress = trs.get(i).select("td").get(3).text();
            String ipType = trs.get(i).select("td").get(5).text();
            String ipSpeed = trs.get(i).select("td").get(6).select("div[class=bar]").attr("title");

            ipMessage.setHost(ipAddress);
            ipMessage.setPort(ipPort);
            ipMessage.setServerAddress(serverAddress);
            ipMessage.setType(ipType);
            ipMessage.setSpeed(ipSpeed);

            list.add(ipMessage);
        }
    }
}