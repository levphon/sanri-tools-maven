package com.sanri.app.translate;

import com.sanri.app.translate.Translate;
import com.sanri.app.translate.TranslateChain;
import com.sanri.app.translate.TranslateCharSequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 符号翻译
 */
public class SymbolTranslate implements Translate {
    private static final Map<String,String> symbolMap = new HashMap<String, String>();
    static {
        symbolMap.put("+","plus");
        symbolMap.put("-","minus");
        symbolMap.put("*","multiply");
        symbolMap.put("/","division");
    }

    @Override
    public void doTranslate(TranslateCharSequence translateCharSequence, TranslateChain translateChain) {
        Set<String> needTranslateWords = translateCharSequence.getNeedTranslateWords();
        for (String needTranslateWord : needTranslateWords) {
            String word = symbolMap.get(needTranslateWord);
            if(word != null){
                translateCharSequence.addTranslate(needTranslateWord,word);
                translateCharSequence.setTranslate(true,needTranslateWord);
            }
        }

        translateChain.doTranslate(translateCharSequence,translateChain);
    }
}
