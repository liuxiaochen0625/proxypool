/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.ipfilter;

import com.wdai.model.IPAdress;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

import static java.lang.System.out;

/**
 * @author reus
 * @version $Id: IPUtils.java, v 0.1 2017-07-20 reus Exp $
 */
public class IPUtils {
    public static List<IPAdress> IPIsable(List<IPAdress> ipMessages) {
        String ip;
        String port;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        for (int i = 0; i < ipMessages.size(); i++) {
            ip = ipMessages.get(i).getHost();
            port = ipMessages.get(i).getPort();

            HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));
            RequestConfig config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(3000).
                    setSocketTimeout(3000).build();
            HttpGet httpGet = new HttpGet("https://www.baidu.com");
            httpGet.setConfig(config);

            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;" +
                    "q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit" +
                    "/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

            try {
                response = httpClient.execute(httpGet);
            } catch (IOException e) {
                out.println("不可用代理已删除" + ipMessages.get(i).getHost() + ": " + ipMessages.get(i).getPort());
                ipMessages.remove(ipMessages.get(i));
                i--;
            }
        }

        try {
            httpClient.close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ipMessages;
    }
}