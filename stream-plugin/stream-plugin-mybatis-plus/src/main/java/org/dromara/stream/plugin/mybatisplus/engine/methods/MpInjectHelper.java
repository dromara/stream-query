package org.dromara.stream.plugin.mybatisplus.engine.methods;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import org.dromara.stream.plugin.mybatisplus.engine.constant.PluginConst;

import java.util.function.Function;

import static com.baomidou.mybatisplus.core.toolkit.Constants.ENTITY;
import static com.baomidou.mybatisplus.core.toolkit.Constants.ENTITY_DOT;

/**
 * <p>MpInjectHelper class.</p>
 *
 * @author VampireAchao
 * @since 2022/10/21 17:42
 */
public class MpInjectHelper {

    private MpInjectHelper() {
        /* Do not new me! */
    }

    /**
     * <p>updateCondition.</p>
     *
     * @param fieldInfo a {@link com.baomidou.mybatisplus.core.metadata.TableFieldInfo} object
     * @param strategy  a {@link java.util.function.Function} object
     * @return a {@link java.lang.String} object
     */
    public static String updateCondition(TableFieldInfo fieldInfo, Function<TableFieldInfo, FieldStrategy> strategy) {
        final FieldStrategy fieldStrategy = strategy.apply(fieldInfo);
        if (fieldStrategy == FieldStrategy.NEVER) {
            return null;
        }
        if (fieldInfo.isPrimitive() || fieldStrategy == FieldStrategy.IGNORED) {
            return Boolean.TRUE.toString();
        }
        if (fieldStrategy == FieldStrategy.NOT_EMPTY && fieldInfo.isCharSequence()) {
            return String.format(PluginConst.NON_BLANK_CONDITION, ENTITY, ENTITY_DOT + fieldInfo.getProperty(), ENTITY_DOT + fieldInfo.getProperty());
        }
        return String.format(PluginConst.NON_NULL_CONDITION, ENTITY, ENTITY_DOT + fieldInfo.getProperty());
    }

}
