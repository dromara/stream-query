package io.github.vampireachao.stream.core.business.highlight;

/**
 * @author VampireAchao
 */
public class FoundWord {

    private String word;
    private String wordAfterHighlight;
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
