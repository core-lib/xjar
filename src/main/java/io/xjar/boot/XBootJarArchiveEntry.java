package io.xjar.boot;

import io.xjar.XConstants;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.util.zip.ZipException;

/**
 * 为了兼容Spring-Boot FatJar 和普通Jar 的包内资源URL一致 所以去掉路径前面的 BOOT-INF/classes/
 *
 * @author Payne 646742615@qq.com
 * 2019/04/23 15:27
 */
public class XBootJarArchiveEntry extends JarArchiveEntry implements XConstants {

    public XBootJarArchiveEntry(ZipArchiveEntry entry) throws ZipException {
        super(entry);
    }

    @Override
    public String getName() {
        return super.getName().substring(BOOT_INF_CLASSES.length());
    }
}