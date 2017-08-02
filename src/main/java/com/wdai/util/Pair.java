/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.util;

/**
 * @author reus
 * @version $Id: Pair.java, v 0.1 2017-04-12 reus Exp $
 */
public class Pair<L,R> {
    /** 左值 */
    private L left;

    /** 右值 */
    private R right;

    public Pair(){

    }

    /**
     * 构造函数
     * @param left
     * @param right
     */
    public Pair(L left, R right){
        this.left = left;
        this.right = right;
    }

    /**
     * Getter method for property <tt>left</tt>.
     *
     * @return property value of left
     */
    public L getLeft() {
        return left;
    }

    /**
     * Setter method for property <tt>left</tt>.
     *
     * @param left value to be assigned to property left
     */
    public void setLeft(L left) {
        this.left = left;
    }

    /**
     * Getter method for property <tt>right</tt>.
     *
     * @return property value of right
     */
    public R getRight() {
        return right;
    }

    /**
     * Setter method for property <tt>right</tt>.
     *
     * @param right value to be assigned to property right
     */
    public void setRight(R right) {
        this.right = right;
    }
}