package io.xjar;

import org.springframework.boot.loader.LaunchedURLClassLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
    private final Set<String> xjars = new LinkedHashSet<>();

    public XClassLoader(URL[] urls, ClassLoader parent) throws IOException {
        super(urls, parent);
        Enumeration<URL> resources = this.getResources(XJAR_INF_DIR + XENC_IDX_FILE);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            System.err.println(resource);
            InputStream in = resource.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            LineNumberReader lnr = new LineNumberReader(isr);
            String line;
            while ((line = lnr.readLine()) != null) {
                xjars.add(line);
            }
        }
    }

    @Override
    public URL findResource(String name) {
        System.out.println("findResource:" + name);
        return super.findResource(name);
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        System.out.println("findResources:" + name);
        return super.findResources(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassFormatError e) {
            throw e;
        }
    }
}
