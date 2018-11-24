package io.xjar.loader;

import io.xjar.XConstants;
import io.xjar.XDecryptor;
import io.xjar.XKit;
import io.xjar.key.XKey;
import org.springframework.boot.loader.LaunchedURLClassLoader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * X类加载器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 23:04
 */
public class XClassLoader extends LaunchedURLClassLoader implements XConstants {
    private final Set<String> indexes = new LinkedHashSet<>();
    private final XURLHandler xURLHandler;

    public XClassLoader(URL[] urls, ClassLoader parent, XDecryptor xDecryptor, XKey xKey) throws IOException {
        super(urls, parent);
        this.xURLHandler = new XURLHandler(xDecryptor, xKey, this);
        Enumeration<URL> resources = this.getResources(XJAR_INF_DIR + XENC_IDX_FILE);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String url = resource.toString();
            String classpath = url.substring(0, url.lastIndexOf("!/") + 2);
            InputStream in = resource.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            LineNumberReader lnr = new LineNumberReader(isr);
            String name;
            while ((name = lnr.readLine()) != null) indexes.add(classpath + name);
        }
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return super.getResources(name);
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null || !indexes.contains(url.toString())) {
            return url;
        }
        try {
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xURLHandler);
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
        return new XEnumeration(enumeration);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassFormatError e) {
            URL resource = findResource(name.replace(".", "/") + ".class");
            if (resource == null) {
                throw new ClassNotFoundException(name, e);
            }
            try (InputStream in = resource.openStream()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                XKit.transfer(in, bos);
                byte[] bytes = bos.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Throwable t) {
                throw new ClassNotFoundException(name, t);
            }
        }
    }

    private class XEnumeration implements Enumeration<URL> {
        private final Enumeration<URL> enumeration;

        XEnumeration(Enumeration<URL> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasMoreElements() {
            return enumeration.hasMoreElements();
        }

        @Override
        public URL nextElement() {
            URL url = enumeration.nextElement();
            if (url == null || !indexes.contains(url.toString())) {
                return url;
            }
            try {
                return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xURLHandler);
            } catch (MalformedURLException e) {
                return url;
            }
        }
    }
}
