package io.xjar.boot;

import io.xjar.XDecryptor;
import io.xjar.XEncryptor;
import io.xjar.XKit;
import io.xjar.key.XKey;
import org.springframework.boot.loader.LaunchedURLClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Enumeration;
import sun.misc.Resource;
import sun.misc.URLClassPath;

/**
 * X类加载器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:04
 */
public class XBootClassLoader extends LaunchedURLClassLoader {
    private final URLClassPath ucp;
    private final XBootURLHandler xBootURLHandler;

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public XBootClassLoader(URL[] urls, ClassLoader parent, XDecryptor xDecryptor, XEncryptor xEncryptor, XKey xKey) throws Exception {
        super(urls, parent);
        this.ucp = new URLClassPath(urls);
        this.xBootURLHandler = new XBootURLHandler(xDecryptor, xEncryptor, xKey, this);
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null) {
            return null;
        }
        try {
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xBootURLHandler);
        } catch (MalformedURLException e) {
            return url;
        }
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        Enumeration<URL> enumeration = super.findResources(name);
        if (enumeration == null) {
            return null;
        }
        return new XBootEnumeration(enumeration);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassFormatError e) {
            String path = name.replace('.',  '/').concat(".class");
            URL resource = findResource(path);
            if (resource == null) {
                throw new ClassNotFoundException(name, e);
            }
            try (InputStream in = resource.openStream()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                XKit.transfer(in, bos);
                byte[] bytes = bos.toByteArray();
                Resource res = ucp.getResource(path, false);
                CodeSource cs = new CodeSource(res.getCodeSourceURL(), res.getCodeSigners());
                return defineClass(name, bytes, 0, bytes.length, cs);
            } catch (Throwable t) {
                throw new ClassNotFoundException(name, t);
            }
        }
    }

    private class XBootEnumeration implements Enumeration<URL> {
        private final Enumeration<URL> enumeration;

        XBootEnumeration(Enumeration<URL> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasMoreElements() {
            return enumeration.hasMoreElements();
        }

        @Override
        public URL nextElement() {
            URL url = enumeration.nextElement();
            if (url == null) {
                return null;
            }
            try {
                return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xBootURLHandler);
            } catch (MalformedURLException e) {
                return url;
            }
        }
    }
}
