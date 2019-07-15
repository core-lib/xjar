package io.xjar.jar;

import io.xjar.*;
import io.xjar.compiler.XStdCompiler;
import io.xjar.digest.XJdkDigestFactory;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
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
    private final XDigestFactory digestFactory;
    private final String digestAlgorithm;
    private final XCompiler compiler;

    public XJarEncryptor(XEncryptor xEncryptor, XEntryFilter<JarArchiveEntry> filter, int level, XDigestFactory digestFactory, String digestAlgorithm, XCompiler compiler) {
        super(xEncryptor, filter);
        this.level = level;
        this.digestFactory = digestFactory;
        this.digestAlgorithm = digestAlgorithm;
        this.compiler = compiler;
    }

    public static void main(String... args) throws Exception {
        XJarEncryptor.builder().build().encrypt(
                XJar.key("io.xjar"),
                "D:\\workspace\\xjar-demo\\target\\xjar-demo-1.0-SNAPSHOT.jar",
                "D:\\workspace\\xjar-demo\\target\\xjar-demo-1.0-SNAPSHOT.xjar"
        );
    }

    @Override
    public void encrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        JarArchiveInputStream zis = null;
        JarArchiveOutputStream zos = null;
        Set<String> indexes = new LinkedHashSet<>();
        XDigest digest = null;
        try {
            digest = digestFactory.acquire(digestAlgorithm);
            zis = new JarArchiveInputStream(in);
            zos = new XDigestedOutputStream(out, digest);
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
                byte[] signature = digest.finish();
                XSignature xSignature = new XSignature(digestAlgorithm, signature);
                File library = compiler.compile(key, xSignature);

            }

            zos.finish();
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        } finally {
            XTool.close(zis);
            XTool.close(zos);
            if (digest != null) {
                digestFactory.release(digestAlgorithm, digest);
            }
        }
    }

    public static XJarEncryptorBuilder builder() {
        return new XJarEncryptorBuilder();
    }

    public static class XJarEncryptorBuilder extends XArchiveEncryptorBuilder<JarArchiveEntry, XJarEncryptor, XJarEncryptorBuilder> {
        private int level = Deflater.DEFLATED;
        private XDigestFactory digestFactory = new XJdkDigestFactory();
        private String digestAlgorithm = "MD5";
        private XCompiler compiler = new XStdCompiler();

        {
            encryptor(new XSmtEncryptor());
            filter(new XJarAllEntryFilter());
        }

        public XJarEncryptorBuilder level(int level) {
            this.level = level;
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

        public XJarEncryptorBuilder compiler(XCompiler compiler) {
            this.compiler = compiler;
            return this;
        }

        @Override
        public XJarEncryptor build() {
            return new XJarEncryptor(encryptor, filter, level, digestFactory, digestAlgorithm, compiler);
        }
    }
}
