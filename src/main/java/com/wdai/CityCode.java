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
            "http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html");
        Page page = downloader.download(request);
        List<Selectable> selectables = page.getHtml().xpath("//*[@class='MsoNormal']").nodes();
        Map<Pair<String, String>, Map<Pair<String, String>, Map<String, String>>> citys = new HashMap<>();
        for (Selectable selectable : selectables) {
            List<Selectable> nodes = selectable.xpath("/p/b").nodes();
            if (nodes != null && nodes.size() > 0) {
                String code = selectable.xpath("/p/b[1]/span/text()").get();
                String city = selectable.xpath("/p/b[2]/span/text()").get();
                if (StringUtils.isBlank(city)) {
                    code = selectable.xpath("/p/span/text()").get();
                    city = selectable.xpath("/p/b[1]/span/text()").get();
                }
                citys.put(new Pair<>(code, city), new HashMap<>());
            } else {
                String secCode = selectable.xpath("/p/span[2]/text()").get().trim();
                String secCity = selectable.xpath("/p/span[3]/text()").get().trim();
                secCity = secCity.replaceAll("　", "");
                String temp = secCode.substring(0, 2) + "0000";
                String str = secCode.substring(0, 4) + "00";
                for (Pair<String, String> pair : citys.keySet()) {
                    if (temp.equals(pair.getLeft())) {
                        if (secCode.endsWith("00"))
                            citys.get(pair).put(new Pair<>(secCode, secCity),
                                new HashMap<String, String>());
                        else {
                            for (Pair<String, String> p : citys.get(pair).keySet())
                                if (str.equals(p.getLeft()))
                                    citys.get(pair).get(p).put(secCode, secCity);
                        }
                    }
                }
            }
        }
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.21.225:3306/db_crawler",
            "***", "***");
        String sql = "INSERT INTO tb_city_code(province_name,province_code,city_name,city_code,county_name,county_code) VALUES (?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (Pair<String, String> pair : citys.keySet()) {
            statement.setString(1, pair.getRight());
            statement.setInt(2, Integer.valueOf(pair.getLeft()));
            if (citys.get(pair).size() == 0) {
                statement.setString(3, null);
                statement.setInt(4, 0);
                statement.setString(5, null);
                statement.setInt(6, 0);
                statement.execute();
            } else {
                for (Pair<String, String> p : citys.get(pair).keySet()) {
                    statement.setString(3, p.getRight());
                    statement.setInt(4, Integer.valueOf(p.getLeft()));
                    if (citys.get(pair).get(p).size() == 0) {
                        statement.setString(5, null);
                        statement.setInt(6, 0);
                        statement.execute();
                    } else {
                        for (Map.Entry<String, String> entry : citys.get(pair).get(p).entrySet()) {
                            statement.setString(5, entry.getValue());
                            statement.setInt(6, Integer.valueOf(entry.getKey()));
                            statement.execute();
                        }
                    }
                }
            }
        }
    }
}