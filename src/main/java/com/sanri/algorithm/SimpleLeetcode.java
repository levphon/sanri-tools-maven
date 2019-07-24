package com.sanri.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class SimpleLeetcode {
    public static String reverseOnlyLetters(String s) {

        return null;
    }

    /**
     * 给定一个非空的字符串，判断它是否可以由它的一个子串重复多次构成。
     * 给定的字符串只含有小写英文字母，并且长度不超过10000。
     * @param s
     * @return
     */
    public static boolean repeatedSubstringPattern(String s){
        int length = s.length();

        for (int i = 0; i < length - 1; i++) {
            if(length %(i + 1) == 0){
                String sub = s.substring(0,i+1);
                String[] split = s.split(sub);
                List<String> newStrArray = new ArrayList<String>();
                for (String subItem : split) {
                    if(!subItem.isEmpty()){
                        newStrArray.add(subItem);
                    }
                }
                if(newStrArray.isEmpty()){
                    return true;
                }
            }
        }

        return false;
    }

    public static long reverse(int x) {
        boolean negtive = false;
        if(x < 0){
            negtive = true;
        }
        int abs = Math.abs(x);
        String source = String.valueOf(abs);
        char[] chars = source.toCharArray();
        char [] newchars = new char[chars.length];
        for (int i = chars.length - 1,j=0; i >= 0  ; i--,j++) {
            newchars[j] = chars[i];
        }
        String s = String.valueOf(newchars);
        long result = Long.parseLong(s);
        if(negtive){
            return 0 - result;
        }
        return result;
    }

    public  static int searchInsert(int[] nums, int target) {
        if(nums .length == 0){
            return 0;
        }
        //比较最大和最小的值的位置
        if(target <= nums[0]){
            return 0;
        }
        if(target >= nums[nums.length - 1]){
            return nums.length;
        }

        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if(target > num){
                continue;
            }
            return i ;
        }

        return nums.length;
    }

    public  static boolean isValid(String s) {
        if(s  == null || s.isEmpty()){
            return true;
        }
        Map<Character,Character> mirrors = new HashMap<Character, Character>();
        mirrors.put('(',')');
        mirrors.put('[',']');
        mirrors.put('{','}');

        char[] chars = s.toCharArray();
        Stack<Character> stack = new Stack();
        stack.push(chars[0]);
        for (char i = 1;i<chars.length;i++) {
            if(stack.isEmpty()){
                stack.push(chars[i]);
                continue;
            }
            Character peek = stack.peek();
            Character character = mirrors.get(peek);
            if(character == null){
                stack.push(chars[i]);
                continue;
            }
            if(chars[i] != character){
                stack.push(chars[i]);
            }else{
                stack.pop();
            }
        }

        return stack.isEmpty();
    }


    public static void main(String[] args) {
//        boolean abab = repeatedSubstringPattern("abaacabaac");
//        System.out.println(abab);

//        long reverse = reverse(1534236469);
//        System.out.println(reverse);
//        System.out.println(Integer.MAX_VALUE);
//        System.out.println(9646324351L);

//        int [] nums = {1,3,5,6};
//        int i = searchInsert(nums, 5);
//        System.out.println(i);


//        System.out.println(isValid("){"));

        System.out.println(5 & 2);
        System.out.println(2 % 5);
        System.out.println(5 % 2);
    }
}
