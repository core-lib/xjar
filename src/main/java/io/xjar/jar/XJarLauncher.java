package io.xjar.jar;

import io.xjar.XConstants;
import io.xjar.XLauncher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * JAR包启动器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/25 18:41
 */
public class XJarLauncher implements XConstants {
    private final XLauncher xLauncher;

    public XJarLauncher(String... args) throws Exception {
        this.xLauncher = new XLauncher(args);
    }

    public static void main(String... args) throws Exception {
        new XJarLauncher(args).launch();
    }

    public void launch() throws Exception {
        XJarClassLoader xJarClassLoader;

        ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            xJarClassLoader = new XJarClassLoader(urlClassLoader.getURLs(), classLoader.getParent(), xLauncher.xDecryptor, xLauncher.xEncryptor, xLauncher.xKey);
        } else {
            ProtectionDomain domain = this.getClass().getProtectionDomain();
            CodeSource source = domain.getCodeSource();
            URI location = (source == null ? null : source.getLocation().toURI());
            String path = (location == null ? null : location.getSchemeSpecificPart());
            if (path == null) {
                throw new IllegalStateException("Unable to determine code source archive");
            }
            String classPath = getJavaClassPath();
            if (null == classPath || classPath.isEmpty()) {
                File jar = new File(path);
                URL url = jar.toURI().toURL();
                xJarClassLoader = new XJarClassLoader(new URL[]{url}, classLoader.getParent(), xLauncher.xDecryptor, xLauncher.xEncryptor, xLauncher.xKey);
            } else {
                List<String> jarPathList = analyzeClassPath(classPath);
                URL[] urlArray = new URL[jarPathList.size()];
                for (int i = 0; i < jarPathList.size(); i++) {
                    String single = jarPathList.get(i);
                    File classPathJar = new File(single);
                    URL classPathUrl = classPathJar.toURI().toURL();
                    urlArray[i] = classPathUrl;
                }
                xJarClassLoader = new XJarClassLoader(urlArray, classLoader.getParent(), xLauncher.xDecryptor, xLauncher.xEncryptor, xLauncher.xKey);
            }
        }

        Thread.currentThread().setContextClassLoader(xJarClassLoader);
        ProtectionDomain domain = this.getClass().getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        URI location = source.getLocation().toURI();
        String filepath = location.getSchemeSpecificPart();
        File file = new File(filepath);
        JarFile jar = new JarFile(file, false);
        Manifest manifest = jar.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        String jarMainClass = attributes.getValue("Jar-Main-Class");
        Class<?> mainClass = xJarClassLoader.loadClass(jarMainClass);
        Method mainMethod = mainClass.getMethod("main", String[].class);
        mainMethod.invoke(null, new Object[]{xLauncher.args});
    }

    private List<String> analyzeClassPath(String classPath) {
        List<String> jarPathList = new ArrayList<>();
        String[] jarPathArryay = classPath.split(File.pathSeparator);
        if (jarPathArryay.length == 0) {
            return jarPathList;
        }
        for (String jarPath : jarPathArryay) {
            if (null == jarPath || jarPath.trim().isEmpty()) {
                continue;
            }
            jarPathList.add(jarPath.trim());
        }
        return jarPathList;
    }

    private String getJavaClassPath() {
        return System.getProperty("java.class.path");
    }
}
