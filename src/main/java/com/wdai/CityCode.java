/**
 * Weidai
 * Copyright (C), 2011 - 2017, 微贷网.
 */
package com.wdai;

import com.wdai.util.ImproveDownloader;
import com.wdai.util.Page;
import com.wdai.util.Pair;
import com.wdai.util.Request;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.selector.Selectable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author reus
 * @version $Id: CityCode.java, v 0.1 2017-08-02 reus Exp $
 */
public class CityCode {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ImproveDownloader downloader = new ImproveDownloader();
        Request request = new Request(
            "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html");
        Page page = downloader.download(request);
        List<Selectable> provinceNodes = page.getHtml().xpath("//*[@class='provincetr']/td")
            .nodes();
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/db_crawler",
            "***", "***");
        String sql = "INSERT INTO tb_city_code_2(province_name,province_code,city_name,city_code,county_name,county_code) VALUES (?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (Selectable provinceNode : provinceNodes) {
            String proviceName = provinceNode.xpath("/td/a/text()").get();
            String provinceUrl = provinceNode.links().get();
            String provinceCode = getCode(provinceUrl);
            request = new Request(provinceUrl);
            page = downloader.download(request);
            List<Selectable> cityNodes = page.getHtml().xpath("//*[@class='citytr']").nodes();
            for (Selectable cityNode : cityNodes) {
                String cityName = cityNode.xpath("/tr/td[2]/a/text()").get();
                String cityUrl = cityNode.xpath("/tr/td[2]").links().get();
                String cityCode = getCode(cityUrl);
                request = new Request(cityUrl);
                page = downloader.download(request);
                List<Selectable> countyNodes = page.getHtml().xpath("//*[@class='countytr']")
                    .nodes();
                for (Selectable countyNode : countyNodes) {
                    String countyName = countyNode.xpath("/tr/td[2]/a/text()").get();
                    if (StringUtils.isBlank(countyName))
                        countyName = countyNode.xpath("/tr/td[2]/text()").get();
                    String countyUrl = countyNode.xpath("/tr/td[2]").links().get();
                    String countyCode;
                    if (StringUtils.isNotBlank(countyUrl))
                        countyCode = getCode(countyUrl);
                    else
                        countyCode = countyNode.xpath("/tr/td[1]/text()").get().substring(0, 6);
                    System.out.println(provinceCode + "-" + proviceName + "=" + cityCode + "-"
                                       + cityName + "-" + countyCode + "-" + countyName);
                    statement.setString(1, proviceName);
                    statement.setInt(2, Integer.valueOf(provinceCode));
                    statement.setString(3, cityName);
                    statement.setInt(4, Integer.valueOf(cityCode));
                    statement.setString(5, countyName);
                    statement.setInt(6, Integer.valueOf(countyCode));
                    statement.execute();
                }
            }
        }
    }

    private static String getCode(String url) {
        int pos = url.lastIndexOf("/") + 1;
        String temp = url.substring(pos).replaceAll("\\.html", "");
        if (temp.length() == 2)
            return temp + "0000";
        else if (temp.length() == 4)
            return temp + "00";
        else
            return temp;
    }
}