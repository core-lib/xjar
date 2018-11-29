package io.xjar.war;

import io.xjar.XConstants;
import io.xjar.XDecryptor;
import io.xjar.XEncryptor;
import io.xjar.key.XKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 加密的URL处理器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/24 13:19
 */
public class XWarURLHandler extends URLStreamHandler implements XConstants {
    private final XDecryptor xDecryptor;
    private final XEncryptor xEncryptor;
    private final XKey xKey;
    private final ClassLoader classLoader;
    private final Set<String> indexes;

    public XWarURLHandler(XDecryptor xDecryptor, XEncryptor xEncryptor, XKey xKey, ClassLoader classLoader) {
        this.xDecryptor = xDecryptor;
        this.xEncryptor = xEncryptor;
        this.xKey = xKey;
        this.classLoader = classLoader;
        this.indexes = new LinkedHashSet<>();
    }

    void init() throws IOException {
        Enumeration<URL> resources = classLoader.getResources(XJAR_INF_DIR + XJAR_INF_IDX);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String url = resource.toString();
            String classpath = resource.getProtocol().equals("file") ? url.substring(0, url.length() - (XJAR_INF_DIR + XJAR_INF_IDX).length()) : url.substring(0, url.lastIndexOf("!/") + 2);
            InputStream in = resource.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            LineNumberReader lnr = new LineNumberReader(isr);
            String name;
            while ((name = lnr.readLine()) != null) indexes.add(classpath + name);
        }
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        URLConnection urlConnection = new URL(url.toString()).openConnection();
        return indexes.contains(url.toString())
                && urlConnection instanceof JarURLConnection
                ? new XWarURLConnection((JarURLConnection) urlConnection, xDecryptor, xEncryptor, xKey)
                : urlConnection;
    }

}
