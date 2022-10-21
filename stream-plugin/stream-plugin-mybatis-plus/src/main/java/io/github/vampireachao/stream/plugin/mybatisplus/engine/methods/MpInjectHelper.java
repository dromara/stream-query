package io.github.vampireachao.stream.plugin.mybatisplus.engine.methods;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;

import java.util.function.Function;

import static com.baomidou.mybatisplus.core.toolkit.Constants.ENTITY;
import static com.baomidou.mybatisplus.core.toolkit.Constants.ENTITY_DOT;
import static io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst.NON_BLANK_CONDITION;
import static io.github.vampireachao.stream.plugin.mybatisplus.engine.constant.PluginConst.NON_NULL_CONDITION;

/**
 * @author VampireAchao
 * @since 2022/10/21 17:42
 */
public class MpInjectHelper {

    private MpInjectHelper() {
        /* Do not new me! */
    }

    public static String updateCondition(TableFieldInfo fieldInfo, Function<TableFieldInfo, FieldStrategy> strategy) {
        final FieldStrategy fieldStrategy = strategy.apply(fieldInfo);
        if (fieldStrategy == FieldStrategy.NEVER) {
            return null;
        }
        if (fieldInfo.isPrimitive() || fieldStrategy == FieldStrategy.IGNORED) {
            return Boolean.TRUE.toString();
        }
        if (fieldStrategy == FieldStrategy.NOT_EMPTY && fieldInfo.isCharSequence()) {
            return String.format(NON_BLANK_CONDITION, ENTITY, ENTITY_DOT + fieldInfo.getProperty(), ENTITY_DOT + fieldInfo.getProperty());
        }
        return String.format(NON_NULL_CONDITION, ENTITY, ENTITY_DOT + fieldInfo.getProperty());
    }

}
