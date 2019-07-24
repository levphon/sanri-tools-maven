package learntest;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

public class FastJsonTest {
    @Test
    public void test(){
        System.out.println(JSONObject.toJSONString(true));
    }
}
