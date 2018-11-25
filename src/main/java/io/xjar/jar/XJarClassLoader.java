package io.xjar.jar;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * JAR包类加载器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/25 22:07
 */
public class XJarClassLoader extends URLClassLoader {

    public XJarClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

}
