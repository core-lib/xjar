package io.xjar;

import io.detector.Resource;
import io.detector.SimpleDetector;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * XJAR API
 *
 * @author Payne 646742615@qq.com
 * 2018/11/25 10:34
 */
public class XJar {

    public static void inject(JarArchiveOutputStream zos) throws IOException {
        String pkg = XKit.class.getPackage().getName().replace('.', '/');
        Collection<Resource> resources = SimpleDetector.Builder
                .scan(pkg)
                .includeJar()
                .recursively()
                .build()
                .detect();
        for (Resource resource : resources) {
            String name = resource.toString();
            name = name.substring(name.lastIndexOf(pkg));
            JarArchiveEntry xEntry = new JarArchiveEntry(name);
            xEntry.setTime(System.currentTimeMillis());
            zos.putArchiveEntry(xEntry);
            try (InputStream ris = resource.getInputStream()) {
                XKit.transfer(ris, zos);
            }
            zos.closeArchiveEntry();
        }
    }

}
