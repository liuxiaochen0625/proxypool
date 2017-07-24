/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.dbutil;

import com.wdai.model.IPAdress;
import com.wdai.model.IPModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author reus
 * @version $Id: DBUtils.java, v 0.1 2017-07-20 reus Exp $
 */
public class DBUtils {
    /** 驱动 */
    private static String driver   = "com.mysql.jdbc.Driver";                                                  //数据库驱动
    private static String dbURL    = "jdbc:mysql://127.0.0.1:3306/IPProxy?characterEncoding=utf8&useSSL=true"; //操作的数据库地址，端口及库名
    private static String user     = "**********";                                                             //数据库用户名
    private static String password = "********";                                                               //数据库密码

    /**
     * 数据库添加功能
     *
     * @param list
     * @throws ClassNotFoundException
     */
    public static void add(List<IPAdress> list) throws ClassNotFoundException {
        Class.forName(driver); //加载数据库驱动

        try (Connection conn = DriverManager.getConnection(dbURL, user, password);
                PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + "ProxyPool (IPAddress, IPPort, serverAddress, IPType, IPSpeed)"
                                                                    + " VALUES (?, ?, ?, ?, ?)")) {

            for (IPAdress ipMessage : list) {
                statement.setString(1, ipMessage.getHost());
                statement.setString(2, ipMessage.getPort());
                statement.setString(3, ipMessage.getServerAddress());
                statement.setString(4, ipMessage.getType());
                statement.setString(5, ipMessage.getSpeed());

                statement.executeUpdate();
            }

            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据库指定IP
     *
     * @param IPid
     */
    public static void deleteIP(int IPid) {
        String sql = "DELETE FROM ProxyPool WHERE id = " + IPid;
        try (Connection conn = DriverManager.getConnection(dbURL, user, password);
                Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);

            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库表清除功能(id也一并清除)
     */
    public static void delete() {
        try (Connection conn = DriverManager.getConnection(dbURL, user, password);
                Statement statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE ProxyPool");

            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库查找功能
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<IPModel> query() throws ClassNotFoundException {
        Class.forName(driver); //加载数据库驱动
        List<IPModel> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbURL, user, password);
                Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ProxyPool");

            while (resultSet.next()) {
                IPModel databaseMessage = new IPModel();

                databaseMessage.setId(resultSet.getString(1));
                databaseMessage.setHost(resultSet.getString(2));
                databaseMessage.setPort(resultSet.getString(3));
                databaseMessage.setServerAddress(resultSet.getString(4));
                databaseMessage.setType(resultSet.getString(5));
                databaseMessage.setSpeed(resultSet.getString(6));

                list.add(databaseMessage);
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}