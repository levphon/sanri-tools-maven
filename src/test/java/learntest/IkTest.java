package learntest;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IkTest {
    @Test
    public void testSplitTest() throws IOException {
        String text = "<三峡人家-清江画廊动车2日游>吊脚楼、民歌和土家幺妹、 住商圈酒店 跟";
        // 创建分词对象
        Analyzer anal = new IKAnalyzer(true); // true　用智能分词，false细粒度
        Configuration cfg = DefaultConfig.getInstance();
//        System.out.println(cfg.getMainDictionary()); // 系统默认词库
//        System.out.println(cfg.getQuantifierDicionary());
        Dictionary.initial(cfg);
        List<String> list = new ArrayList<String>();
        list.add("土家幺妹");
        list.add("2日游");
        list.add("三峡");
        list.add("三峡人家");
        Dictionary.getSingleton().addWords(list);
        StringReader reader = new StringReader(text);
        // 分词
        TokenStream ts = anal.tokenStream("", reader);
        CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
        // 遍历分词数据
        ts.reset();
        while (ts.incrementToken()) {
            System.out.print(term.toString() + "|");
        }
        reader.close();
        System.out.println();
    }
}
