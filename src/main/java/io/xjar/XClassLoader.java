package io.xjar;

import io.xjar.key.XKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.loader.LaunchedURLClassLoader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;
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
    private final Set<String> xjars = new LinkedHashSet<>();
    private final XURLHandler xURLHandler;
    private final XDecryptor xDecryptor;
    private final XKey xKey;

    public XClassLoader(URL[] urls, ClassLoader parent, XDecryptor xDecryptor, XKey xKey) throws IOException {
        super(urls, parent);
        this.xURLHandler = new XURLHandler(xDecryptor, xKey);
        this.xDecryptor = xDecryptor;
        this.xKey = xKey;
        Enumeration<URL> resources = this.getResources(XJAR_INF_DIR + XENC_IDX_FILE);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String url = resource.toString();
            String classpath = url.substring(0, url.lastIndexOf("!/") + 2);
            InputStream in = resource.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            LineNumberReader lnr = new LineNumberReader(isr);
            String name;
            while ((name = lnr.readLine()) != null) xjars.add(classpath + name);
        }
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (!xjars.contains(url.toString())) {
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
        return super.findResources(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassFormatError e) {
            URL resource = findResource(name.replace(".", "/") + ".class");
            try {
                InputStream in = xDecryptor.decrypt(xKey, resource.openStream());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                XKit.transfer(in, bos);
                byte[] bytes = bos.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (IOException ioe) {
                throw e;
            }
        }
    }
}
