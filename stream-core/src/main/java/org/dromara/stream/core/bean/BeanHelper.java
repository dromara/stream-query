package org.dromara.stream.core.bean;


import org.dromara.stream.core.optional.Opp;

import java.util.Objects;

/**
 * <p>BeanHelper class.</p>
 *
 * @author VampireAchao Cizai_

 */
public class BeanHelper {

    /**
     * Constant <code>GETTER_PREFIX="get"</code>
     */
    public static final String GETTER_PREFIX = "get";
    /**
     * Constant <code>GETTER_BOOLEAN_PREFIX="is"</code>
     */
    public static final String GETTER_BOOLEAN_PREFIX = "is";
    /**
     * Constant <code>SETTER_PREFIX="set"</code>
     */
    public static final String SETTER_PREFIX = "set";

    private BeanHelper() {
        /* Do not new me! */
    }

    /**
     * <p>getPropertyName.</p>
     *
     * @param getterOrSetter a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
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
            throw new UnsupportedOperationException(getterOrSetter + " is not getter or setter");
        }
        return originProperty.substring(0, 1).toLowerCase() + originProperty.substring(1);
    }

    /**
     * <p>isGetter.</p>
     *
     * @param methodName a {@link java.lang.String} object
     * @return a boolean
     */
    public static boolean isGetter(String methodName) {
        return Opp.ofStr(methodName).is(s -> s.startsWith(GETTER_PREFIX) || s.startsWith(GETTER_BOOLEAN_PREFIX));
    }

    /**
     * <p>isGetterBoolean.</p>
     *
     * @param methodName a {@link java.lang.String} object
     * @return a boolean
     */
    public static boolean isGetterBoolean(String methodName) {
        return Opp.ofStr(methodName).is(s -> s.startsWith(GETTER_BOOLEAN_PREFIX));
    }

    /**
     * <p>isSetter.</p>
     *
     * @param methodName a {@link java.lang.String} object
     * @return a boolean
     */
    public static boolean isSetter(String methodName) {
        return Opp.ofStr(methodName).is(s -> s.startsWith(SETTER_PREFIX));
    }

    /**
     * 通过属性名得到setter名
     *
     * @param propertyName 属性名
     * @return setter名
     */
    public static String getSetterName(String propertyName) {
        return SETTER_PREFIX + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    /**
     * 通过属性名获取getter名
     *
     * @param propertyName 属性名
     * @return getter名
     */
    public static String getGetterName(String propertyName) {
        return GETTER_PREFIX + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

}
