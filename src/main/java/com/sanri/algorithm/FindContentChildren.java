package com.sanri.algorithm;

import java.util.Arrays;

/**
 * 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。对每个孩子 i ，都有一个胃口值 gi ，
 * 这是能让孩子们满足胃口的饼干的最小尺寸；并且每块饼干 j ，都有一个尺寸 sj 。
 * 如果 sj >= gi ，我们可以将这个饼干 j 分配给孩子 i ，这个孩子会得到满足。
 * 你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
 *   注意：
 *
 *   你可以假设胃口值为正。
 *   一个小朋友最多只能拥有一块饼干。
 *
 *   示例 1:
 *
 *   输入: [1,2,3], [1,1]
 *
 *   输出: 1
 *
 *   解释:
 *   你有三个孩子和两块小饼干，3个孩子的胃口值分别是：1,2,3。
 *   虽然你有两块小饼干，由于他们的尺寸都是1，你只能让胃口值是1的孩子满足。
 *   所以你应该输出1。
 *   示例 2:
 *
 *   输入: [1,2], [1,2,3]
 *
 *   输出: 2
 *
 *   解释:
 *   你有两个孩子和三块小饼干，2个孩子的胃口值分别是1,2。
 *   你拥有的饼干数量和尺寸都足以让所有孩子满足。
 *   所以你应该输出2.
 */
public class FindContentChildren {

    /**
     * 饼干可以合并的; 去掉了 ，每个孩子最多只能给一块饼干 的限制
     * @param g
     * @param s
     * @return
     */
    public static int findContentChildren(int[] g, int[] s) {
        if(s.length  == 0 || g.length == 0){return 0;}
        Arrays.sort(g);
        Arrays.sort(s);

        int i = 0,j = 0,count = 0;
        for(;i<g.length;i++){
            int sum = 0;

            if(j >= s.length){
                break;
            }
            for(;j<s.length;j++){
                sum += s[j];
                if(sum >= g[i]){
                    j++;
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int [] g = {1,2,3};
        int [] s= {1,1,1};
        int test2 = test2(g, s);
        System.out.println(test2);
    }


    /**
     *  正确答案
     * @param g
     * @param s
     * @return
     */
    public static int test2(int[] g, int[] s){
         int child = 0;
        int cookie = 0;
        Arrays.sort(g);  //先将饼干 和 孩子所需大小都进行排序
        Arrays.sort(s);
        while (child < g.length && cookie < s.length ){ //当其中一个遍历就结束
            if (g[child] <= s[cookie]){ //当用当前饼干可以满足当前孩子的需求，可以满足的孩子数量+1
                child++;
            }
            cookie++; // 饼干只可以用一次，因为饼干如果小的话，就是无法满足被抛弃，满足的话就是被用了
        }
        return child;
    }
}
