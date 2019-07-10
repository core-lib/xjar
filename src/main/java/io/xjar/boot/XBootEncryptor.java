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
public class XBootEncryptor extends XArchiveEncryptor<JarArchiveEntry> implements XEncryptor, XConstants {
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

    public XBootEncryptor(XEncryptor xEncryptor, XEntryFilter<JarArchiveEntry> filter, int level) {
        super(xEncryptor, filter);
        this.level = level;
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
            XJarEncryptor xJarEncryptor = XJarEncryptor.builder().build();
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
                        XTool.transfer(nis, eos);
                    }
                }
                // BOOT-INF/lib/**
                else if (entry.getName().startsWith(BOOT_INF_LIB)) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    CheckedOutputStream cos = new CheckedOutputStream(bos, new CRC32());
                    xJarEncryptor.encrypt(key, nis, cos);
                    JarArchiveEntry jar = new JarArchiveEntry(entry.getName());
                    jar.setMethod(JarArchiveEntry.STORED);
                    jar.setSize(bos.size());
                    jar.setTime(entry.getTime());
                    jar.setCrc(cos.getChecksum().getValue());
                    zos.putArchiveEntry(jar);
                    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
                    XTool.transfer(bis, nos);
                }
                // OTHER
                else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    XTool.transfer(nis, nos);
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
                XInjector.inject(zos, "io/xjar/**");
            }

            zos.finish();
        } finally {
            XTool.close(zis);
            XTool.close(zos);
        }
    }

    public static XBootEncryptorBuilder builder() {
        return new XBootEncryptorBuilder();
    }

    public static class XBootEncryptorBuilder extends XArchiveEncryptorBuilder<JarArchiveEntry, XBootEncryptor, XBootEncryptorBuilder> {
        private int level = Deflater.DEFLATED;

        {
            encryptor(new XSmtEncryptor());
            filter(new XJarAllEntryFilter());
        }

        public XBootEncryptorBuilder level(int level) {
            this.level = level;
            return this;
        }

        @Override
        public XBootEncryptor build() {
            return new XBootEncryptor(encryptor, filter, level);
        }
    }
}
