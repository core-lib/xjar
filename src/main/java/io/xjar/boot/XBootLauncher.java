package io.xjar.boot;

import io.xjar.*;
import io.xjar.key.XKey;
import org.springframework.boot.loader.JarLauncher;

import java.io.Console;
import java.net.URL;

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
        if (password == null) {
            Console console = System.console();
            char[] chars = console.readPassword("password:");
            password = new String(chars);
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
