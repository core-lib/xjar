package io.xjar.boot;

import io.xjar.*;
import io.xjar.jar.XJarAllEntryFilter;
import io.xjar.jar.XJarDecryptor;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;

/**
 * Spring-Boot JAR包解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:27
 */
public class XBootDecryptor extends XEntryDecryptor<JarArchiveEntry> implements XDecryptor, XConstants {
    private final int level;

    public XBootDecryptor(XDecryptor xDecryptor) {
        this(xDecryptor, new XJarAllEntryFilter());
    }

    public XBootDecryptor(XDecryptor xDecryptor, XEntryFilter<JarArchiveEntry> filter) {
        this(xDecryptor, Deflater.DEFAULT_COMPRESSION, filter);
    }

    public XBootDecryptor(XDecryptor xDecryptor, int level) {
        this(xDecryptor, level, new XJarAllEntryFilter());
    }

    public XBootDecryptor(XDecryptor xDecryptor, int level, XEntryFilter<JarArchiveEntry> filter) {
        super(xDecryptor, filter);
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
            XUnclosedInputStream nis = new XUnclosedInputStream(zis);
            XUnclosedOutputStream nos = new XUnclosedOutputStream(zos);
            XJarDecryptor xJarDecryptor = new XJarDecryptor(xDecryptor, level, filter);
            JarArchiveEntry entry;
            while ((entry = zis.getNextJarEntry()) != null) {
                if (entry.getName().startsWith(XJAR_SRC_DIR)
                        || entry.getName().endsWith(XJAR_INF_DIR)
                        || entry.getName().endsWith(XJAR_INF_DIR + XJAR_INF_IDX)
                ) {
                    continue;
                }
                // DIR ENTRY
                if (entry.isDirectory()) {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                }
                // META-INF/MANIFEST.MF
                else if (entry.getName().equals(META_INF_MANIFEST)) {
                    Manifest manifest = new Manifest(nis);
                    Attributes attributes = manifest.getMainAttributes();
                    String mainClass = attributes.getValue("Boot-Main-Class");
                    if (mainClass != null) {
                        attributes.putValue("Main-Class", mainClass);
                        attributes.remove(new Attributes.Name("Boot-Main-Class"));
                    }
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    manifest.write(nos);
                }
                // BOOT-INF/classes/**
                else if (entry.getName().startsWith(BOOT_INF_CLASSES)) {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    XBootJarArchiveEntry xBootJarArchiveEntry = new XBootJarArchiveEntry(entry);
                    boolean filtered = filtrate(xBootJarArchiveEntry);
                    XDecryptor decryptor = filtered ? xDecryptor : xNopDecryptor;
                    try (OutputStream eos = decryptor.decrypt(key, nos)) {
                        XKit.transfer(nis, eos);
                    }
                }
                // BOOT-INF/lib/**
                else if (entry.getName().startsWith(BOOT_INF_LIB)) {
                    byte[] data = XKit.read(nis);
                    ByteArrayInputStream lib = new ByteArrayInputStream(data);
                    boolean need = xJarDecryptor.predicate(lib);
                    lib.reset();
                    if (need) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        CheckedOutputStream cos = new CheckedOutputStream(bos, new CRC32());
                        xJarDecryptor.decrypt(key, lib, cos);
                        JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                        jarArchiveEntry.setMethod(JarArchiveEntry.STORED);
                        jarArchiveEntry.setSize(bos.size());
                        jarArchiveEntry.setTime(entry.getTime());
                        jarArchiveEntry.setCrc(cos.getChecksum().getValue());
                        zos.putArchiveEntry(jarArchiveEntry);
                        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                        XKit.transfer(bis, nos);
                    } else {
                        JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                        jarArchiveEntry.setMethod(JarArchiveEntry.STORED);
                        jarArchiveEntry.setSize(entry.getSize());
                        jarArchiveEntry.setTime(entry.getTime());
                        jarArchiveEntry.setCrc(entry.getCrc());
                        zos.putArchiveEntry(jarArchiveEntry);
                        XKit.transfer(lib, nos);
                    }
                }
                // OTHER
                else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    XKit.transfer(nis, nos);
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
