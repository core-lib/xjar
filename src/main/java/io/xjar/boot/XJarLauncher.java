package io.xjar.boot;

import org.springframework.boot.loader.JarLauncher;

import java.net.URL;

/**
 * Spring-Boot Jar 启动器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:06
 */
public class XJarLauncher extends JarLauncher {
    private final XBootLauncher xBootLauncher;

    public XJarLauncher(String... args) throws Exception {
        this.xBootLauncher = new XBootLauncher(args);
    }

    public static void main(String[] args) throws Exception {
        new XJarLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(xBootLauncher.args);
    }

    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws Exception {
        return new XBootClassLoader(urls, this.getClass().getClassLoader(), xBootLauncher.xDecryptor, xBootLauncher.xEncryptor, xBootLauncher.xKey);
    }

}
