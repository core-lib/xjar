package io.xjar.zip;

import io.xjar.*;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;
import java.util.zip.Deflater;

/**
 * ZIP压缩包解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:27
 */
public class XZipDecryptor extends XEntryDecryptor<ZipArchiveEntry> implements XDecryptor {
    private final int level;

    public XZipDecryptor(XDecryptor xDecryptor) {
        this(xDecryptor, null);
    }

    public XZipDecryptor(XDecryptor xDecryptor, XEntryFilter<ZipArchiveEntry> filter) {
        this(xDecryptor, Deflater.DEFAULT_COMPRESSION, filter);
    }

    public XZipDecryptor(XDecryptor xDecryptor, int level) {
        this(xDecryptor, level, null);
    }

    public XZipDecryptor(XDecryptor xDecryptor, int level, XEntryFilter<ZipArchiveEntry> filter) {
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
        ZipArchiveInputStream zis = null;
        ZipArchiveOutputStream zos = null;
        try {
            zis = new ZipArchiveInputStream(in);
            zos = new ZipArchiveOutputStream(out);
            zos.setLevel(level);
            XUnclosedOutputStream nos = new XUnclosedOutputStream(zos);
            ZipArchiveEntry entry;
            while ((entry = zis.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                zos.putArchiveEntry(new ZipArchiveEntry(entry.getName()));
                XDecryptor decryptor = filtrate(entry) ? this : xNopDecryptor;
                try (OutputStream eos = decryptor.decrypt(key, nos)) {
                    XKit.transfer(zis, eos);
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
