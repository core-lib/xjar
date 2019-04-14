package io.xjar.boot;

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
    private final XBootLauncher xBootLauncher;

    public XExtLauncher(String... args) throws Exception {
        this.xBootLauncher = new XBootLauncher(args);
    }

    public static void main(String[] args) throws Exception {
        new XExtLauncher(args).launch();
    }

    public void launch() throws Exception {
        launch(xBootLauncher.args);
    }

    @Override
    protected ClassLoader createClassLoader(List<Archive> archives) throws Exception {
        URLClassLoader classLoader = (URLClassLoader) super.createClassLoader(archives);
        URL[] urls = classLoader.getURLs();
        return new XBootClassLoader(urls, this.getClass().getClassLoader(), xBootLauncher.xDecryptor, xBootLauncher.xEncryptor, xBootLauncher.xKey);
    }
}
