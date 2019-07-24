package com.sanri.algorithm;

import org.junit.Test;

/**
 * 创建时间:2016-10-2下午1:24:39<br/>
 * 创建者:sanri<br/>
 * 功能:分页算法,点击页数时把当前页移动至中间<br/>
 */
public class Fenye {

    @Test
    public void testfen() {
        int listSize = 15;
        int pageNow = 10;
        int beginNum = 0;
        int endNum = 9;
        int pageCount = 15;

        int foot = 0;
        if (pageNow > listSize / 2) {
            foot = (pageNow / (listSize / 2) - 1) * 5 + pageNow % (listSize / 2);
        }
        System.out.println("移动步数: " + foot);
        beginNum = beginNum + foot;
        endNum = beginNum + listSize - 1;
        if (endNum >= pageCount - 1) {
            endNum = pageCount - 1;
        }

        System.out.println("开始页号: " + (beginNum + 1) + " 结束页号: " + (endNum + 1) + " 当前页:" + (pageNow + 1));
    }
}
