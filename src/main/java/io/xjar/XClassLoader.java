package io.xjar;

import org.springframework.boot.loader.LaunchedURLClassLoader;

import java.net.URL;

/**
 * X类加载器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:04
 */
public class XClassLoader extends LaunchedURLClassLoader {

    public XClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
