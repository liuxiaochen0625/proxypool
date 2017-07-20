/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.model;

/**
 * @author reus
 * @version $Id: IPModel.java, v 0.1 2017-07-20 reus Exp $
 */
public class IPModel {
    private String id;
    private String host;
    private String port;
    private String ServerAddress;
    private String type;
    private String speed;

    /**
     * Getter method for property <tt>id</tt>.
     *
     * @return property value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     *
     * @param id value to be assigned to property id
     */
    public void setId(String id) {
        this.id = id;
    }

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
     * Getter method for property <tt>ServerAddress</tt>.
     *
     * @return property value of ServerAddress
     */
    public String getServerAddress() {
        return ServerAddress;
    }

    /**
     * Setter method for property <tt>ServerAddress</tt>.
     *
     * @param ServerAddress value to be assigned to property ServerAddress
     */
    public void setServerAddress(String serverAddress) {
        ServerAddress = serverAddress;
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
}