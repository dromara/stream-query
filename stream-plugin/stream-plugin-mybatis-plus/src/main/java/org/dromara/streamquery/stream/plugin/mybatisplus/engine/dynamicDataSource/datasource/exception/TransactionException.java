/*
 * Copyright © 2018 organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.streamquery.stream.plugin.mybatisplus.engine.dynamicDataSource.datasource.exception;

/**
 * 事务异常
 *
 * @author Hzh
 */
public class TransactionException extends RuntimeException {
    /**
     * 构造
     *
     * @param message 消息
     */
    public TransactionException(String message) {
        super(message);
    }

    /**
     * 构造
     *
     * @param message 消息
     * @param cause   异常
     */
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}