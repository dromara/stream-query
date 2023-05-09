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
 * FoundWord class.
 *
 * @author VampireAchao Cizai_
 */
public class FoundWord {

  /** 查找到的词 */
  private String word;
  /** ignore */
  private String wordAfterHighlight;
  /** 起始下标 */
  private Integer startIndex;
  /** 结束下标 */
  private Integer endIndex;

  /**
   * Constructor for FoundWord.
   *
   * @param word a {@link java.lang.String} object
   * @param index a {@link java.lang.Integer} object
   */
  public FoundWord(String word, Integer index) {
    setWord(word);
    setWordAfterHighlight(word);
    setStartIndex(index);
    setEndIndex(startIndex + word.length() - 1);
  }

  /**
   * Constructor for FoundWord.
   *
   * @param word a {@link java.lang.String} object
   * @param wordAfterHighlight a {@link java.lang.String} object
   * @param startIndex a {@link java.lang.Integer} object
   */
  public FoundWord(String word, String wordAfterHighlight, Integer startIndex) {
    this.word = word;
    this.wordAfterHighlight = wordAfterHighlight;
    this.startIndex = startIndex;
    this.endIndex = startIndex + word.length() - 1;
  }

  /**
   * Getter for the field <code>startIndex</code>.
   *
   * @return a {@link java.lang.Integer} object
   */
  public Integer getStartIndex() {
    return startIndex;
  }

  /**
   * Setter for the field <code>startIndex</code>.
   *
   * @param startIndex a {@link java.lang.Integer} object
   */
  public void setStartIndex(Integer startIndex) {
    this.startIndex = startIndex;
  }

  /**
   * Getter for the field <code>word</code>.
   *
   * @return a {@link java.lang.String} object
   */
  public String getWord() {
    return word;
  }

  /**
   * Setter for the field <code>word</code>.
   *
   * @param word a {@link java.lang.String} object
   */
  public void setWord(String word) {
    this.word = word;
  }

  /**
   * Getter for the field <code>wordAfterHighlight</code>.
   *
   * @return a {@link java.lang.String} object
   */
  public String getWordAfterHighlight() {
    return wordAfterHighlight;
  }

  /**
   * Setter for the field <code>wordAfterHighlight</code>.
   *
   * @param wordAfterHighlight a {@link java.lang.String} object
   */
  public void setWordAfterHighlight(String wordAfterHighlight) {
    this.wordAfterHighlight = wordAfterHighlight;
  }

  /**
   * Getter for the field <code>endIndex</code>.
   *
   * @return a {@link java.lang.Integer} object
   */
  public Integer getEndIndex() {
    return endIndex;
  }

  /**
   * Setter for the field <code>endIndex</code>.
   *
   * @param endIndex a {@link java.lang.Integer} object
   */
  public void setEndIndex(Integer endIndex) {
    this.endIndex = endIndex;
  }
}
