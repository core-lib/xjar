package io.xjar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * XJar File
 *
 * @author Payne 646742615@qq.com
 * 2019/1/25 14:54
 */
public class XJarFile extends JarFile {
    private final ClassLoader classLoader;

    public XJarFile(String name, ClassLoader classLoader) throws IOException {
        super(name);
        this.classLoader = classLoader;
    }

    @Override
    public synchronized InputStream getInputStream(ZipEntry zipEntry) throws IOException {
        final String BOOT_INF_CLASSES = "BOOT-INF/classes/";
        String name = zipEntry.getName();
        if (name.startsWith(BOOT_INF_CLASSES)) {
            URL url = classLoader.getResource(name.substring(BOOT_INF_CLASSES.length()));
            return url != null ? url.openStream() : super.getInputStream(zipEntry);
        }
        return super.getInputStream(zipEntry);
    }
}
