package com.sanri.app.translate;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 标点符号去除翻译
 */
public class PunctuationTranslate implements Translate{
    public static final char [] punctuations = {',','!','-','.'};
    @Override
    public void doTranslate(TranslateCharSequence translateCharSequence, TranslateChain translateChain) {
        //解决词翻译出现的标点符号问题
        Map<String, Set<String>> charMaps = translateCharSequence.getCharMaps();
        Iterator<Map.Entry<String, Set<String>>> iterator = charMaps.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Set<String>> next = iterator.next();
            String key = next.getKey();
            Set<String> values = next.getValue();
            Set<String> newValues = new LinkedHashSet<String>();
            for (String value : values) {
                for (char punctuation : punctuations) {
                    value = value.replace(punctuation,' ');
                }
                String convert2aB = TranslateSupport.convert2aB(value);
                newValues.add(convert2aB);
            }
            charMaps.put(key,newValues);
        }

        //解决直译出现在的标签符号问题
        Set<String> directTranslates = translateCharSequence.getDirectTranslates();
        if(CollectionUtils.isNotEmpty(directTranslates)){
            Iterator<String> directIterator = directTranslates.iterator();
            Set<String> newValues = new LinkedHashSet<String>();
            while (directIterator.hasNext()){
                String value = directIterator.next();
                String newValue = value;
                for (char punctuation : punctuations) {
                    newValue = newValue.replace(punctuation,' ');
                }

                //移除旧值
                if(!value.equals(newValue)){
                    directIterator.remove();;
                }

                String convert2aB = TranslateSupport.convert2aB(newValue);
                newValues.add(convert2aB);
            }

            //添加所有新加的值
            directTranslates.addAll(newValues);
        }


        translateChain.doTranslate(translateCharSequence,translateChain);
    }

    @Test
    public void test(){
        String origin = "drop-addValue-addedServicePlatform";
        for (char punctuation : punctuations) {
            origin = origin.replace(punctuation,' ');
        }
        String convert2aB = TranslateSupport.convert2aB(origin);
        System.out.println(convert2aB);
    }
}
