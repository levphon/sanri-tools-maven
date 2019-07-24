package com.sanri.app.translate;

public interface Translate {

    /**
     * 翻译任务
     * @param translateWord
     */
    void doTranslate(TranslateCharSequence translateCharSequence, TranslateChain translateChain);
}
