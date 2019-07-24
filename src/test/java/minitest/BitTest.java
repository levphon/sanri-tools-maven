package minitest;

import org.junit.Test;

public class BitTest {
    @Test
    public void testRightBit(){
        int a = 15;
        int count = 1 ;
        while( a != 1){
            count++;
            a = a >> 1;
        }
        System.out.println(count);
    }

    public static void main(String[] args) {
        byte b = (byte) 888;
        System.out.println(b);

        System.out.println(888 % 128);
    }

    @Test
    public void test2(){
        String a = "\u0000abcde";
        System.out.println(a.replace('\u0000',' '));
    }

}
