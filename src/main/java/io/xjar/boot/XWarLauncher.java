package io.xjar.boot;

import io.xjar.XLauncher;
import org.springframework.boot.loader.WarLauncher;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Spring-Boot Jar 启动器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:06
 */
public class XWarLauncher extends WarLauncher {
    private final XLauncher xLauncher;

    public XWarLauncher(String... args) throws Exception {
        this.xLauncher = new XLauncher(args);
    }

    public static void main(String[] args) throws Exception {
        new XWarLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(xLauncher.args);
    }

    @Override
    protected void launch(String[] args, String launchClass, ClassLoader classLoader) throws Exception {
        URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
        URL[] urls = urlClassLoader.getURLs();
        ClassLoader cl = new XBootClassLoader(urls, this.getClass().getClassLoader(), xLauncher.xDecryptor, xLauncher.xEncryptor, xLauncher.xKey);
        Thread.currentThread().setContextClassLoader(cl);
        createMainMethodRunner(launchClass, args, classLoader).run();
    }

    /**
     * don't use it
     @Override protected ClassLoader createClassLoader(URL[] urls) throws Exception {
     return new XBootClassLoader(urls, this.getClass().getClassLoader(), xLauncher.xDecryptor, xLauncher.xEncryptor, xLauncher.xKey);
     }
     */

}
