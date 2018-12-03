//package io.xjar.war;
//
//import io.xjar.*;
//import io.xjar.key.XKey;
//import org.apache.catalina.LifecycleException;
//import org.apache.catalina.loader.WebappClassLoader;
//
//import java.io.ByteArrayOutputStream;
//import java.io.Console;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Enumeration;
//
///**
// * Java Web WAR包类加载器
// *
// * @author Payne 646742615@qq.com
// * 2018/11/25 22:07
// */
//public class XWarClassLoader extends WebappClassLoader implements XConstants {
//    static {
//        ClassLoader.registerAsParallelCapable();
//    }
//
//    private final XWarURLHandler xWarURLHandler;
//
//    public XWarClassLoader(ClassLoader parent) throws Exception {
//        super(parent);
//        String algorithm = DEFAULT_ALGORITHM;
//        int keysize = DEFAULT_KEYSIZE;
//        int ivsize = DEFAULT_IVSIZE;
//        String password = null;
//        String[] args = new String[]{""};
//        for (String arg : args) {
//            if (arg.toLowerCase().startsWith(XJAR_ALGORITHM)) {
//                algorithm = arg.substring(XJAR_ALGORITHM.length());
//            }
//            if (arg.toLowerCase().startsWith(XJAR_KEYSIZE)) {
//                keysize = Integer.valueOf(arg.substring(XJAR_KEYSIZE.length()));
//            }
//            if (arg.toLowerCase().startsWith(XJAR_IVSIZE)) {
//                ivsize = Integer.valueOf(arg.substring(XJAR_IVSIZE.length()));
//            }
//            if (arg.toLowerCase().startsWith(XJAR_PASSWORD)) {
//                password = arg.substring(XJAR_PASSWORD.length());
//            }
//        }
//        if (password == null) {
//            Console console = System.console();
//            char[] chars = console.readPassword("password:");
//            password = new String(chars);
//        }
//        XDecryptor xDecryptor = new XJdkDecryptor(algorithm);
//        XEncryptor xEncryptor = new XJdkEncryptor(algorithm);
//        XKey xKey = XKit.key(algorithm, keysize, ivsize, password);
//        this.xWarURLHandler = new XWarURLHandler(xDecryptor, xEncryptor, xKey, this);
//    }
//
//    @Override
//    public void start() throws LifecycleException {
//        super.start();
//        Thread.currentThread().setContextClassLoader(this);
//        try {
//            xWarURLHandler.init();
//        } catch (IOException e) {
//            throw new LifecycleException(e);
//        }
//    }
//
//    @Override
//    public URL findResource(String name) {
//        URL url = super.findResource(name);
//        if (url == null) {
//            return null;
//        }
//        try {
//            return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xWarURLHandler);
//        } catch (MalformedURLException e) {
//            return url;
//        }
//    }
//
//    @Override
//    public Enumeration<URL> findResources(String name) throws IOException {
//        Enumeration<URL> enumeration = super.findResources(name);
//        if (enumeration == null) {
//            return null;
//        }
//        return new XWarEnumeration(enumeration);
//    }
//
//    @Override
//    public Class<?> findClass(String name) throws ClassNotFoundException {
//        try {
//            return super.findClass(name);
//        } catch (Throwable e) {
//            URL resource = findResource(name.replace('.', '/') + ".class");
//            if (resource == null) {
//                throw new ClassNotFoundException(name, e);
//            }
//            try (InputStream in = resource.openStream()) {
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                XKit.transfer(in, bos);
//                byte[] bytes = bos.toByteArray();
//                return defineClass(name, bytes, 0, bytes.length);
//            } catch (Throwable t) {
//                throw new ClassNotFoundException(name, t);
//            }
//        }
//    }
//
//    private class XWarEnumeration implements Enumeration<URL> {
//        private final Enumeration<URL> enumeration;
//
//        XWarEnumeration(Enumeration<URL> enumeration) {
//            this.enumeration = enumeration;
//        }
//
//        @Override
//        public boolean hasMoreElements() {
//            return enumeration.hasMoreElements();
//        }
//
//        @Override
//        public URL nextElement() {
//            URL url = enumeration.nextElement();
//            if (url == null) {
//                return null;
//            }
//            try {
//                return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xWarURLHandler);
//            } catch (MalformedURLException e) {
//                return url;
//            }
//        }
//    }
//}
