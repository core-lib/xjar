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

//    @Override
//    public URL findResource(String name) {
//        System.out.println("findResource:" + name);
//        return super.findResource(name);
//    }
//
//    @Override
//    public Enumeration<URL> findResources(String name) throws IOException {
//        System.out.println("findResources:" + name);
//        return super.findResources(name);
//    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            throw e;
        }
    }
}
