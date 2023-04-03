/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.streamquery.stream.core.business.highlight;

/**
 * <p>FoundWord class.</p>
 *
 * @author VampireAchao Cizai_

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

    /**
     * <p>Constructor for FoundWord.</p>
     *
     * @param word  a {@link java.lang.String} object
     * @param index a {@link java.lang.Integer} object
     */
    public FoundWord(String word, Integer index) {
        setWord(word);
        setWordAfterHighlight(word);
        setIndex(index);
    }

    /**
     * <p>Constructor for FoundWord.</p>
     *
     * @param word               a {@link java.lang.String} object
     * @param wordAfterHighlight a {@link java.lang.String} object
     * @param index              a {@link java.lang.Integer} object
     */
    public FoundWord(String word, String wordAfterHighlight, Integer index) {
        this.word = word;
        this.wordAfterHighlight = wordAfterHighlight;
        this.index = index;
    }

    /**
     * <p>Getter for the field <code>index</code>.</p>
     *
     * @return a {@link java.lang.Integer} object
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * <p>Setter for the field <code>index</code>.</p>
     *
     * @param index a {@link java.lang.Integer} object
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * <p>Getter for the field <code>word</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getWord() {
        return word;
    }

    /**
     * <p>Setter for the field <code>word</code>.</p>
     *
     * @param word a {@link java.lang.String} object
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * <p>Getter for the field <code>wordAfterHighlight</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getWordAfterHighlight() {
        return wordAfterHighlight;
    }

    /**
     * <p>Setter for the field <code>wordAfterHighlight</code>.</p>
     *
     * @param wordAfterHighlight a {@link java.lang.String} object
     */
    public void setWordAfterHighlight(String wordAfterHighlight) {
        this.wordAfterHighlight = wordAfterHighlight;
    }
}
