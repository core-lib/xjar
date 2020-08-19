package io.xjar.boot;

import io.xjar.XLauncher;
import org.springframework.boot.loader.PropertiesLauncher;
import org.springframework.boot.loader.archive.Archive;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Spring-Boot Properties 启动器
 *
 * @author Payne 646742615@qq.com
 * 2019/4/14 10:26
 */
public class XExtLauncher extends PropertiesLauncher {
    private final XLauncher xLauncher;

    public XExtLauncher(String... args) throws Exception {
        this.xLauncher = new XLauncher(args);
    }

    public static void main(String[] args) throws Exception {
        new XExtLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(xLauncher.args);
    }

    /**
     * 查看源码，spring boot 2.3.x 不再调用createClassLoader(List<Archive> archives)，故修改launch方法更合适
     * @param args
     * @param launchClass
     * @param classLoader
     * @throws Exception
     */
    @Override
    protected void launch(String[] args, String launchClass, ClassLoader classLoader) throws Exception {
        URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
        URL[] urls = urlClassLoader.getURLs();
        ClassLoader cl = new XBootClassLoader(urls, this.getClass().getClassLoader(), xLauncher.xDecryptor, xLauncher.xEncryptor, xLauncher.xKey);
        Thread.currentThread().setContextClassLoader(cl);
        createMainMethodRunner(launchClass, args, classLoader).run();
    }

}
