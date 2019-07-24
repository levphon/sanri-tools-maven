package minitest;

import com.sanri.app.translate.TranslateCharSequence;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

public class ToolsSysTest {
    @Test
    public void testTranslate(){
        TranslateCharSequence translateCharSequence = new TranslateCharSequence("我爱你");
        translateCharSequence.addSegment(Arrays.asList("我","是","中国人"));
        translateCharSequence.addSegment(Arrays.asList("我","是","中国","人"));

        translateCharSequence.addTranslate("我","I");
        translateCharSequence.addTranslate("是","am");
        translateCharSequence.addTranslate("是","is");
        translateCharSequence.addTranslate("中国","china");
        translateCharSequence.addTranslate("人","humman");
        translateCharSequence.addTranslate("人","person");
        translateCharSequence.addTranslate("人","db");
        translateCharSequence.addTranslate("中国人","chinaman");

        Set<String> results = translateCharSequence.results();
        System.out.println(results);
    }
}
