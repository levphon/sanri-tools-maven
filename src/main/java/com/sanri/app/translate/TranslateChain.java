package com.sanri.app.translate;


import java.util.LinkedList;
import java.util.List;

public class TranslateChain implements Translate {
    List<Translate> translates = new LinkedList<Translate>();
    private int index = 0;

    /**
     * 添加过滤器
     * @param filter
     */
    public TranslateChain addTranslate(Translate translate){
        translates.add(translate);
        return this;
    }
     /**
     * 翻译任务
     * @param translateCharSequence
     */
   public void doTranslate(TranslateCharSequence translateCharSequence,TranslateChain translateChain){
        if(translateChain.size() ==0 ||  index == translateChain.size()){

            return ;
        }
       Translate translate = translateChain.translates.get(index);
       index++;
        translate.doTranslate(translateCharSequence,translateChain);
    }

    /**
     * 翻译链长度
     * @return
     */
    public int size(){
       return translates.size();
    }

}
