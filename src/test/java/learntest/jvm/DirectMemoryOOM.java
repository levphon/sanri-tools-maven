//package learntest.jvm;
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
///**
// * VM Args : -Xmx20M -XX:MaxDirectMemorySize=10m
// * 本机直接内存溢出
// */
//public class DirectMemoryOOM {
//    private static final int _1MB = 1024 * 1024;
//
//    public static void main(String[] args) throws IllegalAccessException {
//        Field declaredField = Unsafe.class.getDeclaredFields()[0];
//        declaredField.setAccessible(true);
//        Unsafe unsafe = (Unsafe)declaredField.get(null);
//        while (true){
//            unsafe.allocateMemory(_1MB);
//        }
//    }
//}
