package io.xjar.jar;

import io.xjar.*;
import io.xjar.key.XKey;

import java.io.Console;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Scanner;
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
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL url = classLoader.getResource(META_INF_MANIFEST);
        if (url != null) {
            Manifest manifest = new Manifest(url.openStream());
            Attributes attributes = manifest.getMainAttributes();
            if (attributes.getValue(XJAR_ALGORITHM_KEY) != null) {
                algorithm = attributes.getValue(XJAR_ALGORITHM_KEY);
            }
            if (attributes.getValue(XJAR_KEYSIZE_KEY) != null) {
                keysize = Integer.valueOf(attributes.getValue(XJAR_KEYSIZE_KEY));
            }
            if (attributes.getValue(XJAR_IVSIZE_KEY) != null) {
                ivsize = Integer.valueOf(attributes.getValue(XJAR_IVSIZE_KEY));
            }
            if (attributes.getValue(XJAR_PASSWORD_KEY) != null) {
                password = attributes.getValue(XJAR_PASSWORD_KEY);
            }
        }
        if (password == null && System.console() != null) {
            Console console = System.console();
            char[] chars = console.readPassword("password:");
            password = new String(chars);
        }
        if (password == null) {
            System.out.print("password:");
            Scanner scanner = new Scanner(System.in);
            password = scanner.nextLine();
        }
        this.xDecryptor = new XJdkDecryptor(algorithm);
        this.xEncryptor = new XJdkEncryptor(algorithm);
        this.xKey = XKit.key(algorithm, keysize, ivsize, password);
    }

    public static void main(String... args) throws Exception {
        new XJarLauncher(args).launch();
    }

    public void launch() throws Exception {
        ProtectionDomain domain = this.getClass().getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        URI location = (source == null ? null : source.getLocation().toURI());
        String path = (location == null ? null : location.getSchemeSpecificPart());
        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }
        File jar = new File(path);
        URL url = new URL("jar:" + jar.toURI().toURL() + "!/");
        ClassLoader parent = this.getClass().getClassLoader().getParent();
        XJarClassLoader xJarClassLoader = new XJarClassLoader(new URL[]{url}, parent, xDecryptor, xEncryptor, xKey);
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
