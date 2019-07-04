package io.xjar.jar;

import io.xjar.*;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.Deflater;

/**
 * 普通JAR包解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:27
 */
public class XJarDecryptor extends XEntryDecryptor<JarArchiveEntry> implements XDecryptor, XConstants {
    private final int level;

    public XJarDecryptor(XDecryptor xDecryptor) {
        this(xDecryptor, new XJarAllEntryFilter());
    }

    public XJarDecryptor(XDecryptor xDecryptor, XEntryFilter<JarArchiveEntry> filter) {
        this(xDecryptor, Deflater.DEFLATED, filter);
    }

    public XJarDecryptor(XDecryptor xDecryptor, int level) {
        this(xDecryptor, level, new XJarAllEntryFilter());
    }

    public XJarDecryptor(XDecryptor xDecryptor, int level, XEntryFilter<JarArchiveEntry> filter) {
        super(xDecryptor, filter);
        this.level = level;
    }

    public static XJarDecryptorBuilder builder() {
        return new XJarDecryptorBuilder();
    }

    @Override
    public void decrypt(XKey key, String src, String dest) throws IOException {
        decrypt(key, new File(src), new File(dest));
    }

    @Override
    public void decrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        JarArchiveInputStream zis = null;
        JarArchiveOutputStream zos = null;
        try {
            zis = new JarArchiveInputStream(in);
            zos = new JarArchiveOutputStream(out);
            zos.setLevel(level);
            XUnclosedInputStream nis = new XUnclosedInputStream(zis);
            XUnclosedOutputStream nos = new XUnclosedOutputStream(zos);
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
                } else if (entry.getName().equals(META_INF_MANIFEST)) {
                    Manifest manifest = new Manifest(nis);
                    Attributes attributes = manifest.getMainAttributes();
                    String mainClass = attributes.getValue("Jar-Main-Class");
                    if (mainClass != null) {
                        attributes.putValue("Main-Class", mainClass);
                        attributes.remove(new Attributes.Name("Jar-Main-Class"));
                    }
                    XKit.removeKey(attributes);
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    manifest.write(nos);
                } else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    boolean filtered = filtrate(entry);
                    XDecryptor decryptor = filtered ? xDecryptor : xNopDecryptor;
                    try (OutputStream eos = decryptor.decrypt(key, nos)) {
                        XKit.transfer(nis, eos);
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
    public void decrypt(XKey key, File src, File dest) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)
        ) {
            decrypt(key, fis, fos);
        }
    }

    public static class XJarDecryptorBuilder extends XEntryDecryptorBuilder<JarArchiveEntry, XJarDecryptor, XJarDecryptorBuilder> {
        private int level = Deflater.DEFLATED;

        {
            decryptor(new XSmtDecryptor());
            filter(new XJarAllEntryFilter());
        }

        public XJarDecryptorBuilder level(int level) {
            this.level = level;
            return this;
        }

        @Override
        public XJarDecryptor build() {
            return new XJarDecryptor(decryptor, level, filter);
        }
    }
}
