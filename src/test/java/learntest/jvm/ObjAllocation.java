package learntest.jvm;

import org.junit.Test;

public class ObjAllocation {
    public static final int _1MB = 1024 * 1024;

    /**
     * VM Args: -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails
     * -XX:+UseSerialGC
     */
    @Test
    public void testAllocation() {
        byte [] a ,b , c,d ;
        a = new byte[ 2 * _1MB];
        b = new byte[ 2 * _1MB];
        c = new byte[ 2 * _1MB];
        d = new byte[ 4 * _1MB];
    }

    /**
     * VM Args: -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails
     * -XX:+UseSerialGC
     * -XX:PretenureSizeThreshold=3145728  超过 3 M 的对象直接进入老年代
     */
    @Test
    public void testPretenureSizeThreshold(){
        byte [] a =  new byte [4 * _1MB];
    }

    /**
     * 测试长期存活的对象进入老年代
     * VM Args: -Xms20m -Xmx20m -Xmn10m -XX:+PrintGCDetails  -XX:+UseSerialGC
     * -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
     */
    @Test
    public void testTenuringThreshold(){
        byte [] a,b,c;
        a = new byte[ _1MB / 4];
        b = new byte[ 4 * _1MB];
        c = new byte[ 4 * _1MB];
        c = null;
        c = new byte [ 4 * _1MB];
    }

}
