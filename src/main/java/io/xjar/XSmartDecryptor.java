package io.xjar;

import io.xjar.boot.XBootDecryptor;
import io.xjar.jar.XJarDecryptor;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * 智能解密器
 *
 * @author Payne 646742615@qq.com
 * 2020/4/27 16:39
 */
public class XSmartDecryptor extends XEntryDecryptor<JarArchiveEntry> implements XDecryptor, XConstants {

    public XSmartDecryptor(XDecryptor xDecryptor) {
        super(xDecryptor);
    }

    public XSmartDecryptor(XDecryptor xDecryptor, XEntryFilter<JarArchiveEntry> filter) {
        super(xDecryptor, filter);
    }

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        try (JarFile jar = new JarFile(src, false)) {
            Manifest manifest = jar.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            String version = attributes.getValue("Spring-Boot-Version");
            XDecryptor decryptor = version != null ? new XBootDecryptor(xDecryptor, filter) : new XJarDecryptor(xDecryptor, filter);
            decryptor.decrypt(key, src, dest);
        }
    }

    @Override
    public void decrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        throw new UnsupportedOperationException();
    }
}
