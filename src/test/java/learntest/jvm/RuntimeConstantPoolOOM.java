package learntest.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -XX:PermSize=2m -XX:MaxPermSize=2m
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        int i=0;
        while(true)
            list.add(String.valueOf(i++).intern());
    }
}
