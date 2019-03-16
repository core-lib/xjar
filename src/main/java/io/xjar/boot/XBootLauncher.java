package io.xjar.boot;

import io.xjar.*;
import io.xjar.key.XKey;
import org.springframework.boot.loader.JarLauncher;

import java.io.Console;
import java.net.URL;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Spring-Boot启动器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:06
 */
public class XBootLauncher extends JarLauncher implements XConstants {
    private final String[] args;
    private final XDecryptor xDecryptor;
    private final XEncryptor xEncryptor;
    private final XKey xKey;

    public XBootLauncher(String... args) throws Exception {
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

    public static void main(String[] args) throws Exception {
        new XBootLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(args);
    }

    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws Exception {
        return new XBootClassLoader(urls, this.getClass().getClassLoader(), xDecryptor, xEncryptor, xKey);
    }

}
