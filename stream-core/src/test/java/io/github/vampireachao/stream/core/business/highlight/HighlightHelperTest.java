package io.github.vampireachao.stream.core.business.highlight;

import cn.hutool.dfa.WordTree;
import io.github.vampireachao.stream.core.lambda.function.SerUnOp;
import io.github.vampireachao.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 高亮封装测试
 *
 * @author VampireAchao Cizai_
 */
class HighlightHelperTest {

    @Test
    void highlight() {
        String text = "我有一颗大土豆，刚出锅的";
        List<FoundWord> foundWords = Steam.of(
                new FoundWord("大", 4),
                new FoundWord("大土豆", 4),
                new FoundWord("土豆", 5),
                new FoundWord("刚出锅", 8),
                new FoundWord("出锅", 9)
        ).toList();
        String highlight = HighlightHelper.highlight(text, foundWords, s -> "<span style='color:red'>" + s + "</span>");
        Assertions.assertEquals("我有一颗<span style='color:red'>大土豆</span>，<span style='color:red'>刚出锅</span>的", highlight);

        Assertions.assertEquals("", HighlightHelper.highlight("", new ArrayList<>(), SerUnOp.identity()));
    }


    @Test
    void containsStringTest() {
        WordTree tree = new WordTree();
        tree.addWord("大土豆");
        tree.addWord("土豆呀");
        tree.addWord("刚出锅");
        String text = "我有一颗大土豆呀，刚出锅的";
        List<cn.hutool.dfa.FoundWord> foundWords = tree.matchAllWords(text, -1, true, true);
        final List<io.github.vampireachao.stream.core.business.highlight.FoundWord> foundWordList = Steam.of(foundWords).map(w -> new io.github.vampireachao.stream.core.business.highlight.FoundWord(w.getWord(), w.getStartIndex())).toList();
        String result = HighlightHelper.highlight(text, foundWordList, s -> "<" + s + ">");
        Assertions.assertEquals("我有一颗<大土豆呀>，<刚出锅>的", result);
    }

}
