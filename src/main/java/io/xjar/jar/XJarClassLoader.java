package io.xjar.jar;

import io.xjar.XDecryptor;
import io.xjar.XEncryptor;
import io.xjar.XKit;
import io.xjar.key.XKey;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.Enumeration;

/**
 * JAR包类加载器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/25 22:07
 */
public class XJarClassLoader extends URLClassLoader {
    private final XJarURLHandler xJarURLHandler;
    private final URLClassPath urlClassPath;

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public XJarClassLoader(URL[] urls, ClassLoader parent, XDecryptor xDecryptor, XEncryptor xEncryptor, XKey xKey) throws Exception {
        super(urls, parent);
        this.xJarURLHandler = new XJarURLHandler(xDecryptor, xEncryptor, xKey, this);
        Field ucpField = URLClassLoader.class.getDeclaredField("ucp");
        ucpField.setAccessible(true);
        this.urlClassPath = (URLClassPath) ucpField.get(this);
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null) {
            return null;
        }
        try {
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xJarURLHandler);
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
        return new XJarEnumeration(enumeration);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassFormatError e) {
            String path = name.replace('.', '/').concat(".class");
            URL url = findResource(path);
            if (url == null) {
                throw new ClassNotFoundException(name, e);
            }
            try (InputStream in = url.openStream()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                XKit.transfer(in, bos);
                byte[] bytes = bos.toByteArray();
                Resource resource = urlClassPath.getResource(path);
                CodeSource codeSource = new CodeSource(resource.getCodeSourceURL(), resource.getCodeSigners());
                return defineClass(name, bytes, 0, bytes.length, codeSource);
            } catch (Throwable t) {
                throw new ClassNotFoundException(name, t);
            }
        }
    }

    private class XJarEnumeration implements Enumeration<URL> {
        private final Enumeration<URL> enumeration;

        XJarEnumeration(Enumeration<URL> enumeration) {
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
                return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xJarURLHandler);
            } catch (MalformedURLException e) {
                return url;
            }
        }
    }
}
