package io.xjar;

import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.Manifest;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;

/**
 * JAR包加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:27
 */
public class XJarEncryptor extends XEntryEncryptor<JarArchiveEntry> implements XEncryptor, XConstants {
    private final int level;

    public XJarEncryptor(XEncryptor xEncryptor) {
        this(xEncryptor, (Collection<XEntryFilter<JarArchiveEntry>>) null);
    }

    public XJarEncryptor(XEncryptor xEncryptor, XEntryFilter<JarArchiveEntry>... filters) {
        this(xEncryptor, Arrays.asList(filters));
    }

    public XJarEncryptor(XEncryptor xEncryptor, Collection<XEntryFilter<JarArchiveEntry>> filters) {
        this(xEncryptor, Deflater.DEFLATED, filters);
    }

    public XJarEncryptor(XEncryptor xEncryptor, int level) {
        this(xEncryptor, level, (Collection<XEntryFilter<JarArchiveEntry>>) null);
    }

    public XJarEncryptor(XEncryptor xEncryptor, int level, XEntryFilter<JarArchiveEntry>... filters) {
        this(xEncryptor, level, Arrays.asList(filters));
    }

    public XJarEncryptor(XEncryptor xEncryptor, int level, Collection<XEntryFilter<JarArchiveEntry>> filters) {
        super(xEncryptor, filters);
        this.level = level;
    }

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)
        ) {
            encrypt(key, fis, fos);
        }
    }

    @Override
    public void encrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        JarArchiveInputStream zis = null;
        JarArchiveOutputStream zos = null;
        Set<String> indexes = new LinkedHashSet<>();
        try {
            zis = new JarArchiveInputStream(in);
            zos = new JarArchiveOutputStream(out);
            zos.setLevel(level);
            NoCloseInputStream nis = new NoCloseInputStream(zis);
            NoCloseOutputStream nos = new NoCloseOutputStream(zos);
            XJarEncryptor xJarEncryptor = new XJarEncryptor(xEncryptor, level, xAlwaysFilter);
            JarArchiveEntry entry;
            Manifest manifest = null;
            while ((entry = zis.getNextJarEntry()) != null) {
                if (entry.getName().startsWith(XJAR_INF_DIR)) {
                    continue;
                }
                if (entry.isDirectory()) {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                } else if (entry.getName().endsWith(".jar")) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    CheckedOutputStream cos = new CheckedOutputStream(bos, new CRC32());
                    boolean filtered = filter(entry);
                    XEncryptor encryptor = filtered ? xJarEncryptor : xNopEncryptor;
                    encryptor.encrypt(key, nis, cos);
                    JarArchiveEntry jar = new JarArchiveEntry(entry.getName());
                    jar.setMethod(JarArchiveEntry.STORED);
                    jar.setSize(bos.size());
                    jar.setTime(entry.getTime());
                    jar.setCrc(cos.getChecksum().getValue());
                    zos.putArchiveEntry(jar);
                    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                    XKit.transfer(bis, zos);
                } else if (entry.getName().equals("META-INF/MANIFEST.MF")) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    XKit.transfer(zis, bos);
                    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                    manifest = XKit.manifest(bis);
                    bis.reset();
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    boolean filtered = filter(entry);
                    if (filtered) indexes.add(entry.getName());
                    XEncryptor encryptor = filtered ? this : xNopEncryptor;
                    try (OutputStream eos = encryptor.encrypt(key, nos)) {
                        XKit.transfer(bis, eos);
                    }
                } else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    boolean filtered = filter(entry);
                    if (filtered) indexes.add(entry.getName());
                    XEncryptor encryptor = filtered ? this : xNopEncryptor;
                    try (OutputStream eos = encryptor.encrypt(key, nos)) {
                        XKit.transfer(zis, eos);
                    }
                }
                zos.closeArchiveEntry();
            }

            if (!indexes.isEmpty()) {
                String classpath = manifest != null && manifest.getMainAttributes() != null ? manifest.getMainAttributes().getValue("Spring-Boot-Classes") : null;
                JarArchiveEntry XJAR_INF = new JarArchiveEntry((classpath != null ? classpath : "") + XJAR_INF_DIR);
                XJAR_INF.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(XJAR_INF);
                zos.closeArchiveEntry();

                JarArchiveEntry XENC_IDX = new JarArchiveEntry((classpath != null ? classpath : "") + XJAR_INF_DIR + XENC_IDX_FILE);
                XENC_IDX.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(XENC_IDX);
                int idx = classpath != null ? classpath.length() : 0;
                for (String index : indexes) {
                    zos.write(index.substring(idx).getBytes());
                    zos.write(CRLF.getBytes());
                }
                zos.closeArchiveEntry();
            }

            zos.finish();
        } finally {
            XKit.close(zis);
            XKit.close(zos);
        }
    }
}
