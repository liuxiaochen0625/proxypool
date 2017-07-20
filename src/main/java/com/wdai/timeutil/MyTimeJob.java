/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.timeutil;

import com.wdai.dbutil.DBUtils;
import com.wdai.ipfilter.IPFilter;
import com.wdai.ipfilter.IPUtils;
import com.wdai.model.IPAdress;
import com.wdai.model.IPModel;
import com.wdai.parse.URLFecter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * @author reus
 * @version $Id: MyTimeJob.java, v 0.1 2017-07-20 reus Exp $
 */
public class MyTimeJob implements Job {
    @Override
    public void execute(JobExecutionContext argv) throws JobExecutionException {
        List<String> Urls = new ArrayList<>();
        List<IPModel> databaseMessages = new ArrayList<>();
        List<IPAdress> list = new ArrayList<>();
        List<IPAdress> ipMessages = new ArrayList<>();
        String url = "http://www.xicidaili.com/nn/1";
        String IPAddress;
        String IPPort;
        int k, j;

        //首先使用本机ip进行爬取
        try {
            list = URLFecter.urlParse(url, list);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //对得到的IP进行筛选，选取链接速度前100名的
        list = IPFilter.Filter(list);

        //构造种子Url
        for (int i = 1; i <= 5; i++) {
            Urls.add("http://www.xicidaili.com/nn/" + i);
        }

        //得到所需要的数据
        for (k = 0, j = 0; j < Urls.size(); k++) {
            url = Urls.get(j);

            IPAddress = list.get(k).getHost();
            IPPort = list.get(k).getPort();
            //每次爬取前的大小
            int preIPMessSize = ipMessages.size();
            try {
                ipMessages = URLFecter.urlParse(url, IPAddress, IPPort, ipMessages);
                //每次爬取后的大小
                int lastIPMessSize = ipMessages.size();
                if (preIPMessSize != lastIPMessSize) {
                    j++;
                }

                //对IP进行轮寻调用
                if (k >= list.size() - 1) {
                    k = 0;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //对得到的IP进行筛选，选取链接速度前100名的
        ipMessages = IPFilter.Filter(ipMessages);

        //对ip进行测试，不可用的从数组中删除
        ipMessages = IPUtils.IPIsable(ipMessages);

        for (IPAdress ipMessage : ipMessages) {
            out.println(ipMessage.getHost());
            out.println(ipMessage.getPort());
            out.println(ipMessage.getServerAddress());
            out.println(ipMessage.getType());
            out.println(ipMessage.getSpeed());
        }

        //将得到的IP存储在数据库中(每次先清空数据库)
        try {
            DBUtils.delete();
            DBUtils.add(ipMessages);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //从数据库中将IP取到
        try {
            databaseMessages = DBUtils.query();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (IPModel databaseMessage : databaseMessages) {
            out.println(databaseMessage.getId());
            out.println(databaseMessage.getHost());
            out.println(databaseMessage.getPort());
            out.println(databaseMessage.getServerAddress());
            out.println(databaseMessage.getType());
            out.println(databaseMessage.getSpeed());
        }
    }

    public static void main(String[] args) throws JobExecutionException {
        MyTimeJob job = new MyTimeJob();
        job.execute(null);
    }
}