/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.model;

/**
 * @author reus
 * @version $Id: IPAdress.java, v 0.1 2017-07-20 reus Exp $
 */
public class IPAdress {
    /** IP地址 */
    private String host;

    /** 端口号 */
    private String port;

    /** IP所在地区 */
    private String serverAddress;

    /** 代理类型：HTTP和HTTPS */
    private String type;

    /** 代理速度 */
    private String speed;

    /**
     * Getter method for property <tt>host</tt>.
     *
     * @return property value of host
     */
    public String getHost() {
        return host;
    }

    /**
     * Setter method for property <tt>host</tt>.
     *
     * @param host value to be assigned to property host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Getter method for property <tt>port</tt>.
     *
     * @return property value of port
     */
    public String getPort() {
        return port;
    }

    /**
     * Setter method for property <tt>port</tt>.
     *
     * @param port value to be assigned to property port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Getter method for property <tt>serverAddress</tt>.
     *
     * @return property value of serverAddress
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Setter method for property <tt>serverAddress</tt>.
     *
     * @param serverAddress value to be assigned to property serverAddress
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     *
     * @param type value to be assigned to property type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter method for property <tt>speed</tt>.
     *
     * @return property value of speed
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * Setter method for property <tt>speed</tt>.
     *
     * @param speed value to be assigned to property speed
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "IPAdress{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", type='" + type + '\'' +
                ", speed='" + speed + '\'' +
                '}';
    }
}