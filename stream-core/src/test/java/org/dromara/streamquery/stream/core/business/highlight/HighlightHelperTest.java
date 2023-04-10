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

import org.dromara.streamquery.stream.core.collection.Lists;
import org.dromara.streamquery.stream.core.stream.Steam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 高亮封装测试
 *
 * @author VampireAchao Cizai_
 */
class HighlightHelperTest {

  @Test
  void highlight() {
    String text = "我有一颗大土豆呀，刚出锅的";
    List<FoundWord> foundWords =
        Steam.of(
                new FoundWord("大", 4),
                new FoundWord("大土豆", 4),
                new FoundWord("土豆", 5),
                new FoundWord("刚出锅", 9),
                new FoundWord("出锅", 10))
            .toList();
    String highlight =
        HighlightHelper.highlight(
            text, foundWords, s -> "<span style='color:red'>" + s + "</span>");
    Assertions.assertEquals(
        "我有一颗<span style='color:red'>大土豆</span>呀，<span style='color:red'>刚出锅</span>的", highlight);

    foundWords =
        Lists.of(new FoundWord("大土豆", 4), new FoundWord("土豆呀", 5), new FoundWord("刚出锅", 9));
    highlight =
        HighlightHelper.highlight(
            text, foundWords, s -> "<span style='color:red'>" + s + "</span>");
    Assertions.assertEquals(
        "我有一颗<span style='color:red'>大土豆呀</span>，<span style='color:red'>刚出锅</span>的", highlight);
  }
}
