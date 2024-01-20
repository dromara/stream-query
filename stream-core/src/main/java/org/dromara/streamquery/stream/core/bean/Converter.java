package org.dromara.streamquery.stream.core.bean;

/**
 * Converter
 *
 * @author achao@apache.org
 */
public interface Converter<S, T> {
    T convert(S source);
}
