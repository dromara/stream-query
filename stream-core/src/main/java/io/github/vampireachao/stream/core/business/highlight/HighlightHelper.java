package io.github.vampireachao.stream.core.business.highlight;

import io.github.vampireachao.stream.core.optional.Opp;
import io.github.vampireachao.stream.core.stream.Steam;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * 高亮封装
 *
 * @author VampireAchao Cizai_

 */
public class HighlightHelper {

    private HighlightHelper() {
        /* Do not new me! */
    }

    /**
     * <p>highlight.</p>
     *
     * @param text              a {@link java.lang.String} object
     * @param fondWords         a {@link java.util.List} object
     * @param highlightOperator a {@link java.util.function.UnaryOperator} object
     * @return a {@link java.lang.String} object
     */
    public static String highlight(String text, List<FoundWord> fondWords, UnaryOperator<String> highlightOperator) {
        if (Opp.ofColl(fondWords).isEmpty() || Opp.ofStr(text).isEmpty()) {
            return text;
        }
        fondWords = Steam.of(fondWords)
                .sorted(Comparator.comparing(FoundWord::getIndex)
                        .thenComparingInt(w -> w.getWord().length()))
                .toList();
        LinkedList<FoundWord> linkedList = new LinkedList<>();
        // 记录历史下标(当前拼接到整体string的哪个位置了)
        int lastIdx = 0;
        // 进行遍历所有结果(理论上已按照 startIndex和fondWord.length排序)
        for (int i = 0; i < fondWords.size(); i++) {
            FoundWord fondWord = fondWords.get(i);
            String word = null;
            // 如果本次匹配发现历史下标已经超过了当前匹配值，说明这次词语和上次词语下标重复或者是上次的一部分
            if (i > 0 && lastIdx > fondWord.getIndex()) {
                // 移除拼接的额外部分
                FoundWord last = Objects.requireNonNull(linkedList.pollLast());
                // 判断上次和这次的长度，如果上次长度大于这次长度，说明这次词语是上次的一部分
                lastIdx -= last.getWord().length();
                if (last.getWord().length() > fondWord.getWord().length()) {
                    // 直接使用上次的词汇以及重置历史下标
                    word = last.getWord();
                }
            } else {
                // 否则根据历史下标到当前词汇下标进行查找额外部分
                String partOne = text.substring(lastIdx, fondWord.getIndex());
                // 并且将历史下标往前推进
                lastIdx += partOne.length();
                // 将额外部分拼接到链表中
                linkedList.add(new FoundWord(partOne, lastIdx));
            }
            // 获取本次需要高亮的词汇
            word = Opp.ofStr(word).orElseGet(fondWord::getWord);
            // 历史下标往前推进
            lastIdx += word.length();
            // 执行高亮操作
            linkedList.add(new FoundWord(word, highlightOperator.apply(word), lastIdx));
        }
        // 别忘了加上最后一截
        linkedList.add(new FoundWord(text.substring(lastIdx), text.substring(lastIdx), lastIdx));
        return Steam.of(linkedList).map(FoundWord::getWordAfterHighlight).join();
    }


}
