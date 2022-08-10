package io.github.vampireachao.stream.core.lambda;

import io.github.vampireachao.stream.core.bean.BeanHelper;
import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.optional.Opp;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;


/**
 * LambdaHelper
 *
 * @author VampireAchao
 * @since 2022/5/29 9:19
 */
public class LambdaHelper {

    private static final String CONSTRUCTOR_METHOD_NAME = "<init>";

    private static final WeakHashMap<String, LambdaExecutable> SERIALIZED_LAMBDA_EXECUTABLE_CACHE = new WeakHashMap<>();

    private LambdaHelper() {
        /* Do not new me! */
    }

    /**
     * Resolve the lambda to a {@link SerializedLambda} instance.
     *
     * @param lambda The lambda to resolve.
     * @return SerializedLambda
     */
    private static SerializedLambda serialize(Serializable lambda) {
        if (lambda instanceof SerializedLambda) {
            return (SerializedLambda) lambda;
        }
        final Class<? extends Serializable> clazz = lambda.getClass();
        if (!clazz.isSynthetic()) {
            throw new IllegalArgumentException("Not a lambda expression: " + clazz.getName());
        }
        try {
            final Method writeReplace = ReflectHelper.accessible(clazz.getDeclaredMethod("writeReplace"));
            final Object maybeSerLambda = writeReplace.invoke(lambda);
            if (Objects.nonNull(maybeSerLambda) && maybeSerLambda instanceof SerializedLambda) {
                return (SerializedLambda) maybeSerLambda;
            }
            throw new IllegalStateException("writeReplace result value is not java.lang.invoke.SerializedLambda");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * Resolve the lambda to a {@link LambdaExecutable} instance.
     *
     * @param lambda The lambda to resolve.
     * @return LambdaExecutable
     */
    public static <T extends Serializable> LambdaExecutable resolve(T lambda) {
        Objects.requireNonNull(lambda, "lambda can not be null");
        if (lambda instanceof Proxy) {
            InvocationHandler handler = Proxy.getInvocationHandler(lambda);
            MethodHandle methodHandle = ReflectHelper.getFieldValue(handler, "val$target");
            System.out.println("methodHandle: " + methodHandle + "fields: " + Steam.of(methodHandle.getClass().getDeclaredFields()).map(Field::getName).toMap(Function.identity(), name -> ReflectHelper.getFieldValue(methodHandle, name)));
            Member member = ReflectHelper.getFieldValue(methodHandle, "member");
            System.out.println("member: " + member);
            System.out.println(Steam.of(member.getClass().getDeclaredFields()).map(Field::getName).toMap(Function.identity(), fieldName -> ReflectHelper.getFieldValue(member, fieldName)));
            Class<?> clazz = member.getDeclaringClass();
            System.out.println("clazz: " + clazz);
            String name = member.getName();
            System.out.println("name: " + name);
            MethodType type = ReflectHelper.getFieldValue(member, "type");
            System.out.println("type: " + type + "type.getClass()): " + type.getClass());
            System.out.println(Steam.of(type.getClass().getDeclaredFields()).map(Field::getName).toMap(Function.identity(), fieldName -> ReflectHelper.getFieldValue(type, fieldName)));
            System.out.println("toMethodDescriptorString: " + type.toMethodDescriptorString());

            if (CONSTRUCTOR_METHOD_NAME.equals(name)) {
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    if (ReflectHelper.getDescriptor(constructor).equals(type.toMethodDescriptorString())) {
                        return new LambdaExecutable(constructor, type.toMethodDescriptorString());
                    }
                }
            } else {
                List<Method> methods = ReflectHelper.getAllDeclaredMethods(clazz);
                for (Method method : methods) {
                    if (method.getName().equals(name)
                            && ReflectHelper.getDescriptor(method).equals(type.toMethodDescriptorString())) {
                        return new LambdaExecutable(method, type.toMethodDescriptorString());
                    }
                }
            }
        }
        return SERIALIZED_LAMBDA_EXECUTABLE_CACHE.computeIfAbsent(lambda.getClass().getName(), key -> {
            final SerializedLambda serializedLambda = serialize(lambda);
            final String methodName = serializedLambda.getImplMethodName();
            final Class<?> implClass;
            try {
                implClass = Class.forName(serializedLambda.getImplClass().replace("/", "."), true, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
            if (CONSTRUCTOR_METHOD_NAME.equals(methodName)) {
                for (Constructor<?> constructor : implClass.getDeclaredConstructors()) {
                    if (ReflectHelper.getDescriptor(constructor).equals(serializedLambda.getImplMethodSignature())) {
                        return new LambdaExecutable(constructor, serializedLambda);
                    }
                }
            } else {
                List<Method> methods = ReflectHelper.getAllDeclaredMethods(implClass);
                for (Method method : methods) {
                    if (method.getName().equals(methodName)
                            && ReflectHelper.getDescriptor(method).equals(serializedLambda.getImplMethodSignature())) {
                        return new LambdaExecutable(method, serializedLambda);
                    }
                }
            }
            throw new IllegalStateException("No lambda method found.");
        });
    }

    @SafeVarargs
    public static <T> String[] getPropertyNames(SerFunc<T, ?>... funcs) {
        return Steam.of(funcs).map(LambdaHelper::getPropertyName).toArray(String[]::new);
    }

    public static <T> String getPropertyName(SerFunc<T, ?> func) {
        return Opp.of(func).map(LambdaHelper::resolve).map(LambdaExecutable::getName).map(BeanHelper::getPropertyName).get();
    }

}
