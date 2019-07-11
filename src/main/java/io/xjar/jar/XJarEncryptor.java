package io.xjar.jar;

import io.xjar.*;
import io.xjar.digest.XJdkDigestFactory;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.Deflater;

/**
 * 普通JAR包加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:27
 */
public class XJarEncryptor extends XArchiveEncryptor<JarArchiveEntry> implements XEncryptor, XConstants {
    private final int level;
    private final String jdkLocation;
    private final String gccLocation;
    private final String tmpLocation;
    private final XDigestFactory digestFactory;
    private final String digestAlgorithm;

    public XJarEncryptor(XEncryptor xEncryptor, XEntryFilter<JarArchiveEntry> filter, int level, String jdkLocation, String gccLocation, String tmpLocation, XDigestFactory digestFactory, String digestAlgorithm) {
        super(xEncryptor, filter);
        this.level = level;
        this.jdkLocation = jdkLocation;
        this.gccLocation = gccLocation;
        this.tmpLocation = tmpLocation;
        this.digestFactory = digestFactory;
        this.digestAlgorithm = digestAlgorithm;
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
            JarArchiveEntry entry;
            Manifest manifest = null;
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
                    manifest = new Manifest(nis);
                    Attributes attributes = manifest.getMainAttributes();
                    String mainClass = attributes.getValue("Main-Class");
                    if (mainClass != null) {
                        attributes.putValue("Jar-Main-Class", mainClass);
                        attributes.putValue("Main-Class", "io.xjar.jar.XJarLauncher");
                    }
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    manifest.write(nos);
                } else {
                    JarArchiveEntry jarArchiveEntry = new JarArchiveEntry(entry.getName());
                    jarArchiveEntry.setTime(entry.getTime());
                    zos.putArchiveEntry(jarArchiveEntry);
                    boolean filtered = filtrate(entry);
                    if (filtered) {
                        indexes.add(entry.getName());
                    }
                    XEncryptor encryptor = filtered ? xEncryptor : xNopEncryptor;
                    try (OutputStream eos = encryptor.encrypt(key, nos)) {
                        XTool.transfer(nis, eos);
                    }
                }
                zos.closeArchiveEntry();
            }

            if (!indexes.isEmpty()) {
                JarArchiveEntry xjarInfDir = new JarArchiveEntry(XJAR_INF_DIR);
                xjarInfDir.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(xjarInfDir);
                zos.closeArchiveEntry();

                JarArchiveEntry xjarInfIdx = new JarArchiveEntry(XJAR_INF_DIR + XJAR_INF_IDX);
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

    public static XJarEncryptorBuilder builder() {
        return new XJarEncryptorBuilder();
    }

    public static class XJarEncryptorBuilder extends XArchiveEncryptorBuilder<JarArchiveEntry, XJarEncryptor, XJarEncryptorBuilder> {
        private int level = Deflater.DEFLATED;
        private String jdkLocation = new File(System.getProperty("java.home")).getParent();
        private String gccLocation = "g++";
        private String tmpLocation = System.getProperty("java.io.tmpdir");
        private XDigestFactory digestFactory = new XJdkDigestFactory();
        private String digestAlgorithm = "MD5";

        {
            encryptor(new XSmtEncryptor());
            filter(new XJarAllEntryFilter());
        }

        public XJarEncryptorBuilder level(int level) {
            this.level = level;
            return this;
        }

        public XJarEncryptorBuilder jdkLocation(String jdkLocation) {
            this.jdkLocation = jdkLocation;
            return this;
        }

        public XJarEncryptorBuilder gccLocation(String gccLocation) {
            this.gccLocation = gccLocation;
            return this;
        }

        public XJarEncryptorBuilder tmpLocation(String tmpLocation) {
            this.tmpLocation = tmpLocation;
            return this;
        }

        public XJarEncryptorBuilder digestFactory(XDigestFactory digestFactory) {
            this.digestFactory = digestFactory;
            return this;
        }

        public XJarEncryptorBuilder digestAlgorithm(String digestAlgorithm) {
            this.digestAlgorithm = digestAlgorithm;
            return this;
        }

        @Override
        public XJarEncryptor build() {
            return new XJarEncryptor(encryptor, filter, level, jdkLocation, gccLocation, tmpLocation, digestFactory, digestAlgorithm);
        }
    }
}
