package io.github.vampireachao.stream.core.clazz;

import io.github.vampireachao.stream.core.lambda.function.SerFunc;
import io.github.vampireachao.stream.core.lambda.function.SerSupp;
import io.github.vampireachao.stream.core.reflect.ReflectHelper;
import io.github.vampireachao.stream.core.stream.Steam;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * ClassHelper
 *
 * @author VampireAchao
 * @since 2023/1/9
 */
public class ClassHelper {

    private ClassHelper() {
        /* Do not new me! */
    }

    /**
     * 扫描对应包下的类
     *
     * @param packageName 包名，例如io.github.vampireachao.stream.core.clazz
     * @return 包下的类
     */
    public static List<Class<?>> scanClasses(String packageName) {
        Enumeration<URL> resources = ((SerSupp<Enumeration<URL>>) () ->
                ClassLoader.getSystemClassLoader().getResources(packageName.replace(".", "/"))
        ).get();
        return Steam.of(Collections.list(resources))
                .map(URL::getFile)
                .map((SerFunc<String, File>) f -> new File(URLDecoder.decode(f, StandardCharsets.UTF_8.name())))
                .filter(dir -> dir.exists() && dir.isDirectory())
                .map(File::listFiles)
                .flat(files -> Steam.of(files).map(File::getAbsolutePath)
                        .filter(path -> path.endsWith(".class"))
                        .map(path -> path.substring(path.lastIndexOf("\\") + 1, path.length() - 6))
                        .<Class<?>>map(className -> ReflectHelper.loadClass(packageName + "." + className)))
                .toList();
    }

}
