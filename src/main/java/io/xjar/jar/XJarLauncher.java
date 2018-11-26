package io.xjar.jar;

import io.xjar.*;
import io.xjar.key.XKey;

import java.io.Console;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * JAR包启动器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/25 18:41
 */
public class XJarLauncher implements XConstants {
    private final String[] args;
    private final XDecryptor xDecryptor;
    private final XEncryptor xEncryptor;
    private final XKey xKey;

    public XJarLauncher(String... args) throws Exception {
        this.args = args;
        String algorithm = DEFAULT_ALGORITHM;
        int keysize = DEFAULT_KEYSIZE;
        int ivsize = DEFAULT_IVSIZE;
        String password = null;
        for (String arg : args) {
            if (arg.toLowerCase().startsWith(XJAR_ALGORITHM)) {
                algorithm = arg.substring(XJAR_ALGORITHM.length());
            }
            if (arg.toLowerCase().startsWith(XJAR_KEYSIZE)) {
                keysize = Integer.valueOf(arg.substring(XJAR_KEYSIZE.length()));
            }
            if (arg.toLowerCase().startsWith(XJAR_IVSIZE)) {
                ivsize = Integer.valueOf(arg.substring(XJAR_IVSIZE.length()));
            }
            if (arg.toLowerCase().startsWith(XJAR_PASSWORD)) {
                password = arg.substring(XJAR_PASSWORD.length());
            }
        }
        if (password == null) {
            Console console = System.console();
            char[] chars = console.readPassword("password:");
            password = new String(chars);
        }
        this.xDecryptor = new XJdkDecryptor(algorithm);
        this.xEncryptor = new XJdkEncryptor(algorithm);
        this.xKey = XKit.key(algorithm, keysize, ivsize, password);
    }

    public static void main(String... args) throws Exception {
        new XJarLauncher(args).launch();
    }

    public void launch() throws Exception {
        String dir = System.getProperty("java.user.dir");
        String path = System.getProperty("java.class.path");
        File file = new File(dir, path);
        URL url = new URL("jar:" + file.toURI().toURL() + "!/");
        XJarClassLoader xJarClassLoader = new XJarClassLoader(new URL[]{url}, this.getClass().getClassLoader().getParent(), xDecryptor, xEncryptor, xKey);
        Thread.currentThread().setContextClassLoader(xJarClassLoader);
        URL resource = xJarClassLoader.findResource(META_INF_MANIFEST);
        try (InputStream in = resource.openStream()) {
            Manifest manifest = new Manifest(in);
            Attributes attributes = manifest.getMainAttributes();
            String jarMainClass = attributes.getValue("Jar-Main-Class");
            Class<?> mainClass = xJarClassLoader.loadClass(jarMainClass);
            Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{args});
        }
    }

}
