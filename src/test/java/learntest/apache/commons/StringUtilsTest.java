package learntest.apache.commons;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void leftPad(){
        String s = StringUtils.leftPad("1", 3, '0');
        System.out.println(s);
    }
}
