package com.sanri.algorithm;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

public class SortAll {
    int [] nums = new int [10];

    @Before
    public void  numberProvider(){
        for (int i = 0; i < nums.length; i++) {
            nums[i] = RandomUtils.nextInt(0,1000);
        }
        System.out.println("排序前:"+ StringUtils.join(nums,','));
    }

    @Test
    public void testSyso(){

    }

    @Test
    public void testInsertSort(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        //从第一个开始,因为第一个已经不用排序
        for (int i = 1; i < nums.length; i++) {
            int currValue = nums[i];
            int k = i;
            while (k >= 1  && nums[k - 1] > currValue){
                //当上一个值还比我大的时候往前遍历,直到找到第一个比我小的,我就应该插入那个位置
                //当前元素后移一个位置,刚好是占用我的位置
                nums[k] = nums[k - 1];
                k--;
            }
            //找到了当前位置 ,插入元素;  如果元素不需要移动,则还是赋值为本身位置
            nums[k] = currValue;
        }

        stopWatch.stop();
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
//        System.out.println("排序后:"+StringUtils.join(nums,','));
    }

    @Test
    public void testInsertSortNet(){
        //用哨兵做时需要先把元素后移
        for (int i = 0; i < nums.length - 1; i++) {
            nums[i + 1] = nums[i];
        }
        nums [ 0] = -1;

        for (int i = 2; i < nums.length; i++) {
            nums[0] = nums[i];
            int  j= i-1;
            for(;nums[j] > nums[0] ;j--){
                nums[j + 1] = nums[j];
            }
            nums[j +1] = nums[0];
        }
        System.out.println("排序后:"+StringUtils.join(nums,','));
    }

    /**
     * 使用哨兵的插入排序
     * 每次循环少了一次判断 将 k >= 1  && nums[k - 1] > currValue 减少为一次判断
     */
    @Test
    public void testInsertUseGuard(){
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        for (int i = 2; i < nums.length; i++) {
            //将 nums[0] 做为哨兵

        }
        stopWatch.stop();
        System.out.println("排序用时:"+stopWatch.getNanoTime()+" nanoTime ");
        System.out.println("排序后:"+StringUtils.join(nums,','));
    }

    @Test
    public void testBubblingSort(){
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if(nums[i] > nums[j]){
                    int temp = nums[j];
                    nums[j] = nums[i];
                    nums[i] = temp;
                }
                System.out.println("比较后:"+StringUtils.join(nums,','));
            }
        }
        System.out.println("排序后:"+StringUtils.join(nums,','));
    }
}
