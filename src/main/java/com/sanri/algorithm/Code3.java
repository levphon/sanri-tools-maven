package com.sanri.algorithm;

import java.util.HashMap;
import java.util.Map;

public class Code3 {
	public static void main(String[] args) {
		char [] arr = {'h','e','l','l','o','c','o','m','p','u','t','e','r','o'};
		Map<String,Integer> chars = new HashMap<String, Integer>();
		for (char c : arr) {
			if(chars.get(String.valueOf(c)) == null){
				chars.put(String.valueOf(c), 0);
			}else{
				int count = chars.get(String.valueOf(c));
				chars.put(String.valueOf(c), ++count);
			}
		}
		
	}
}