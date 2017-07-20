/**
 * Weidai
 * Copyright (c) 2017-2017 All Rights Reserved.
 */
package com.wdai.ipfilter;

import com.wdai.model.IPAdress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author reus
 * @version $Id: IPFilter.java, v 0.1 2017-07-20 reus Exp $
 *          <p>
 *          对于Java已经规定的常用的类如String我们不可能对它进行重新编译，在不能使用Comparable
 *          的情况下我们需要自己操作Comparator，重新定义它的compare方法.
 *          <p>
 *          String的compareTo方法自动升序排列.
 */

public class IPFilter {
    //对IP进行过滤，选取1000个IP中速度排名前六百的IP(升序)，其余的舍弃
    public static List<IPAdress> Filter(List<IPAdress> list) {
        List<IPAdress> newlist = new ArrayList<>();

        Collections.sort(list, new Comparator<IPAdress>() {
            @Override
            public int compare(IPAdress o1, IPAdress o2) {
                return o1.getSpeed().compareTo(o2.getSpeed());
            }
        });

        //只返回容器中前100的对象
        for (int i = 0; i < list.size(); i++) {
            if (i < 100) {
                newlist.add(list.get(i));
            } else {
                break;
            }
        }

        return newlist;
    }
}