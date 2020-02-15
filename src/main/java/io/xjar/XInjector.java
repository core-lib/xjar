package io.xjar;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * XJAR API
 *
 * @author Payne 646742615@qq.com
 * 2018/11/25 10:34
 */
public class XInjector {

    /**
     * 往JAR包中注入XJar框架、以及XJar依赖的classes
     * @param zos jar包输出流
     * @throws IOException I/O 异常
     */
    public static void inject(JarArchiveOutputStream zos) throws IOException {
        inject(Loaders.ant().load("io/xjar/**"), zos);
        inject(Loaders.ant().load("javassist/**"), zos);
    }

    /**
     * 往JAR包中注入Resource
     * @param resources
     * @param zos
     * @throws IOException
     */
    private static void inject(Enumeration<Resource> resources, JarArchiveOutputStream zos) throws IOException{
        Set<String> directories = new HashSet<>();
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            String name = resource.getName();
            String directory = name.substring(0, name.lastIndexOf('/') + 1);
            if (directories.add(directory)) {
                JarArchiveEntry xDirEntry = new JarArchiveEntry(directory);
                xDirEntry.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(xDirEntry);
                zos.closeArchiveEntry();
            }
            JarArchiveEntry xJarEntry = new JarArchiveEntry(name);
            xJarEntry.setTime(System.currentTimeMillis());
            zos.putArchiveEntry(xJarEntry);
            try (InputStream ris = resource.getInputStream()) {
                XKit.transfer(ris, zos);
            }
            zos.closeArchiveEntry();
        }
    }
}
