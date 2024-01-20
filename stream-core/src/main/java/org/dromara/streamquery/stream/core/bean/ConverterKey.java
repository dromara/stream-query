package org.dromara.streamquery.stream.core.bean;

import java.util.Objects;

/**
 * ConverterKey
 *
 * @author achao@apache.org
 */
public class ConverterKey {
    private Class<?> sourceType;
    private Class<?> targetType;

    public Class<?> getSourceType() {
        return sourceType;
    }

    public void setSourceType(Class<?> sourceType) {
        this.sourceType = sourceType;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConverterKey that = (ConverterKey) o;
        return Objects.equals(sourceType, that.sourceType) && Objects.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceType, targetType);
    }
}
