package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * JNI 接口
 *
 * @author Payne 646742615@qq.com
 * 2019/7/16 13:22
 */
public class XJni {
    private static volatile XJni instance;

    private XJni() throws IOException {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final URL url = classLoader.getResource("XJAR.SO");
        if (url == null) {
            throw new IllegalStateException("xjar library not found");
        }
        final File lib = File.createTempFile("XJAR", ".SO");
        try (final InputStream in = url.openStream()) {
            XTool.transfer(in, lib);
        }
        System.load(lib.getCanonicalPath());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                XTool.delete(lib);
            }
        });
    }

    public static XJni getInstance() throws IOException {
        if (instance != null) {
            return instance;
        }
        synchronized (XJni.class) {
            if (instance != null) {
                return instance;
            }
            instance = new XJni();
            return instance;
        }
    }

    public native XKey call();

}
