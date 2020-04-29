package io.xjar.boot;

import io.xjar.*;
import io.xjar.jar.XJarAllEntryFilter;
import io.xjar.jar.XJarEncryptor;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;

/**
 * Spring-Boot JAR包加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:27
 */
public class XBootEncryptor extends XEntryEncryptor<JarArchiveEntry> implements XEncryptor, XConstants {
    private final Map<String, String> map = new HashMap<>();

    {
        final String jarLauncher = "org.springframework.boot.loader.JarLauncher";
        final String warLauncher = "org.springframework.boot.loader.WarLauncher";
        final String extLauncher = "org.springframework.boot.loader.PropertiesLauncher";
        map.put(jarLauncher, "io.xjar.boot.XJarLauncher");
        map.put(warLauncher, "io.xjar.boot.XWarLauncher");
        map.put(extLauncher, "io.xjar.boot.XExtLauncher");
    }

    private final int level;

    public XBootEncryptor(XEncryptor xEncryptor) {
        this(xEncryptor, new XJarAllEntryFilter());
    }

    public XBootEncryptor(XEncryptor xEncryptor, XEntryFilter<JarArchiveEntry> filter) {
        this(xEncryptor, Deflater.DEFAULT_COMPRESSION, filter);
    }

    public XBootEncryptor(XEncryptor xEncryptor, int level) {
        this(xEncryptor, level, new XJarAllEntryFilter());
    }

    public XBootEncryptor(XEncryptor xEncryptor, int level, XEntryFilter<JarArchiveEntry> filter) {
        super(xEncryptor, filter);
        this.level = level;
    }

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dest)
        ) {
            encrypt(key, fis, fos);
            XGo.make(dest, key);
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
            XUnclosedInputStream nis = new XUnclosedInputStream(zis);
            XUnclosedOutputStream nos = new XUnclosedOutputStream(zos);
            XJarEncryptor xJarEncryptor = new XJarEncryptor(xEncryptor, level, filter);
            JarArchiveEntry entry;
            Manifest manifest = null;
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
                    manifest = new Manifest(nis);
                    Attributes attributes = manifest.getMainAttributes();
                    String mainClass = attributes.getValue("Main-Class");
                    if (mainClass != null) {
                        attributes.putValue("Boot-Main-Class", mainClass);
                        attributes.putValue("Main-Class", map.get(mainClass));
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
                    if (filtered) {
                        indexes.add(xBootJarArchiveEntry.getName());
                    }
                    XEncryptor encryptor = filtered ? xEncryptor : xNopEncryptor;
                    try (OutputStream eos = encryptor.encrypt(key, nos)) {
                        XKit.transfer(nis, eos);
                    }
                }
                // BOOT-INF/lib/**
                else if (entry.getName().startsWith(BOOT_INF_LIB)) {
                    byte[] data = XKit.read(nis);
                    ByteArrayInputStream lib = new ByteArrayInputStream(data);
                    boolean need = xJarEncryptor.predicate(lib);
                    lib.reset();
                    if (need) {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        CheckedOutputStream cos = new CheckedOutputStream(bos, new CRC32());
                        xJarEncryptor.encrypt(key, lib, cos);
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

            if (!indexes.isEmpty()) {
                JarArchiveEntry xjarInfDir = new JarArchiveEntry(BOOT_INF_CLASSES + XJAR_INF_DIR);
                xjarInfDir.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(xjarInfDir);
                zos.closeArchiveEntry();

                JarArchiveEntry xjarInfIdx = new JarArchiveEntry(BOOT_INF_CLASSES + XJAR_INF_DIR + XJAR_INF_IDX);
                xjarInfIdx.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(xjarInfIdx);
                for (String index : indexes) {
                    zos.write(index.getBytes());
                    zos.write(CRLF.getBytes());
                }
                zos.closeArchiveEntry();
            }

            String mainClass = manifest != null && manifest.getMainAttributes() != null ? manifest.getMainAttributes().getValue("Main-Class") : null;
            if (mainClass != null) {
                XInjector.inject(zos);
            }

            zos.finish();
        } finally {
            XKit.close(zis);
            XKit.close(zos);
        }
    }

}
