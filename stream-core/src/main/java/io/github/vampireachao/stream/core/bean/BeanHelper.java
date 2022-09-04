package io.github.vampireachao.stream.core.bean;

import io.github.vampireachao.stream.core.optional.StrOp;

import java.util.Objects;

/**
 * @author VampireAchao
 */
public class BeanHelper {

    public static final String GETTER_PREFIX = "get";
    public static final String GETTER_BOOLEAN_PREFIX = "is";
    public static final String SETTER_PREFIX = "set";

    private BeanHelper() {
        /* Do not new me! */
    }

    public static String getPropertyName(String getterOrSetter) {
        String originProperty = null;
        if (isGetterBoolean(getterOrSetter)) {
            originProperty = getterOrSetter.replaceFirst(GETTER_BOOLEAN_PREFIX, "");
        } else if (isGetter(getterOrSetter)) {
            originProperty = getterOrSetter.replaceFirst(GETTER_PREFIX, "");
        } else if (isSetter(getterOrSetter)) {
            originProperty = getterOrSetter.replaceFirst(SETTER_PREFIX, "");
        }
        if (Objects.isNull(originProperty)) {
            throw new UnsupportedOperationException(getterOrSetter + "is not getter or setter");
        }
        return originProperty.substring(0, 1).toLowerCase() + originProperty.substring(1);
    }

    public static boolean isGetter(String methodName) {
        return StrOp.of(methodName).is(s -> s.startsWith(GETTER_PREFIX) || s.startsWith(GETTER_BOOLEAN_PREFIX));
    }

    public static boolean isGetterBoolean(String methodName) {
        return StrOp.of(methodName).is(s -> s.startsWith(GETTER_BOOLEAN_PREFIX));
    }

    public static boolean isSetter(String methodName) {
        return StrOp.of(methodName).is(s -> s.startsWith(SETTER_PREFIX));
    }

}
