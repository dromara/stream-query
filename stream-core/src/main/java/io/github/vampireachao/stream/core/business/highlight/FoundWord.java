package io.github.vampireachao.stream.core.business.highlight;

/**
 * @author VampireAchao &lt; achao1441470436@gmail.com &gt; <br/> ZVerify &lt; 2556450572@qq.com &gt;
 */
public class FoundWord {

    /**
     * 查找到的词
     */
    private String word;
    /**
     * ignore
     */
    private String wordAfterHighlight;
    /**
     * 下标
     */
    private Integer index;

    public FoundWord(String word, Integer index) {
        this.word = word;
        this.wordAfterHighlight = word;
        this.index = index;
    }

    public FoundWord(String word, String wordAfterHighlight, Integer index) {
        this.word = word;
        this.wordAfterHighlight = wordAfterHighlight;
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordAfterHighlight() {
        return wordAfterHighlight;
    }

    public void setWordAfterHighlight(String wordAfterHighlight) {
        this.wordAfterHighlight = wordAfterHighlight;
    }
}
