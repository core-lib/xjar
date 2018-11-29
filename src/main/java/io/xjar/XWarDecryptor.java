package io.xjar;

import io.xjar.key.XKey;
import io.xjar.war.XWarAllFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.Deflater;

/**
 * Java Web WAR包解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:27
 */
public class XWarDecryptor extends XEntryDecryptor<JarArchiveEntry> implements XDecryptor, XConstants {
    // 安全过滤器，避免由于用户自定义过滤器时把其他无关资源加密了造成无法运行
    private final XJarArchiveEntryFilter safeFilter = new XWarAllFilter();
    private final int level;

    public XWarDecryptor(XDecryptor xEncryptor, XJarArchiveEntryFilter... filters) {
        this(xEncryptor, Arrays.asList(filters));
    }

    public XWarDecryptor(XDecryptor xDecryptor, Collection<XJarArchiveEntryFilter> filters) {
        this(xDecryptor, Deflater.DEFLATED, filters);
    }

    public XWarDecryptor(XDecryptor xEncryptor, int level, XJarArchiveEntryFilter... filters) {
        this(xEncryptor, level, Arrays.asList(filters));
    }

    public XWarDecryptor(XDecryptor xDecryptor, int level, Collection<XJarArchiveEntryFilter> filters) {
        super(xDecryptor, filters);
        this.level = level;
    }

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)
        ) {
            decrypt(key, fis, fos);
        }
    }

    @Override
    public void decrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        JarArchiveInputStream zis = null;
        JarArchiveOutputStream zos = null;
        try {
            zis = new JarArchiveInputStream(in);
            zos = new JarArchiveOutputStream(out);
            zos.setLevel(level);
            NoCloseInputStream nis = new NoCloseInputStream(zis);
            NoCloseOutputStream nos = new NoCloseOutputStream(zos);
            XJarDecryptor xJarDecryptor = new XJarDecryptor(xDecryptor, level);
            JarArchiveEntry entry;
            while ((entry = zis.getNextJarEntry()) != null) {
                if (entry.getName().startsWith(XJAR_SRC_DIR)
                        || entry.getName().endsWith(XJAR_INF_DIR)
                        || entry.getName().endsWith(XJAR_INF_DIR + XJAR_INF_IDX)
                ) {
                    continue;
                }
                if (entry.isDirectory()) {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                } else if (entry.getName().endsWith(".jar")) {
                    JarArchiveEntry jar = new JarArchiveEntry(entry.getName());
                    jar.setTime(entry.getTime());
                    zos.putArchiveEntry(jar);
                    boolean filtered = filter(entry);
                    XDecryptor decryptor = filtered ? xJarDecryptor : xNopDecryptor;
                    decryptor.decrypt(key, nis, nos);
                } else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    boolean filtered = filter(entry);
                    XDecryptor decryptor = filtered ? this : xNopDecryptor;
                    try (InputStream eis = decryptor.decrypt(key, nis)) {
                        XKit.transfer(eis, nos);
                    }
                }
                zos.closeArchiveEntry();
            }

            zos.finish();
        } finally {
            XKit.close(zis);
            XKit.close(zos);
        }
    }

    @Override
    public boolean filter(JarArchiveEntry entry) {
        return super.filter(entry) && safeFilter.filter(entry);
    }
}
