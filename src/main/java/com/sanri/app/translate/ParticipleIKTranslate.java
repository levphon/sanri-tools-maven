package com.sanri.app.translate;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 分词器,IK 分词
 */
public class ParticipleIKTranslate implements Translate {
    Analyzer ikAnalyzer = new IKAnalyzer(true); // true　用智能分词，false细粒度
    Configuration cfg = DefaultConfig.getInstance();
    {
        Dictionary.initial(cfg);
    }

    @Override
    public void doTranslate(TranslateCharSequence translateCharSequence, TranslateChain translateChain) {
        StringReader reader = new StringReader(translateCharSequence.getOriginSequence().toString());
        // 分词
        TokenStream tokenStream = null;
        try {
            tokenStream = ikAnalyzer.tokenStream("", reader);
            CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
            // 遍历分词数据
            tokenStream.reset();
            List<String> segment = new ArrayList<String>();
            while (tokenStream.incrementToken()) {
                segment.add(term.toString());
            }
            translateCharSequence.addSegment(segment);

            //初始化翻译
            translateCharSequence.initCharMaps();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(reader);
            translateChain.doTranslate(translateCharSequence,translateChain);
        }
    }
}
