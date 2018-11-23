package io.xjar;

import io.xjar.key.XKey;

import java.io.*;
import java.util.UUID;
import java.util.zip.Deflater;

/**
 * Spring Boot JAR 加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 11:10
 */
public class XSbjDecryptor extends XWrappedDecryptor implements XDecryptor {

    public XSbjDecryptor(XDecryptor xDecryptor) {
        super(xDecryptor);
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
        File tmp = new File(System.getProperty("java.io.tmpdir"));
        File dir = new File(tmp, UUID.randomUUID().toString());
        File src = new File(dir, "src");
        File enc = new File(dir, "enc");
        // 解压
        XKit.unpack(in, src);
        // 加密
        XJarDecryptor xJarDecryptor = new XJarDecryptor(xDecryptor, Deflater.NO_COMPRESSION);
        File lib = new File(src, "BOOT-INF/lib");
        File dst = new File(enc, "BOOT-INF/lib");
        if (!dst.exists() && !dst.mkdirs()) {
            throw new IOException("could not make directory:" + dst);
        }
        File[] libs = lib.listFiles();
        for (int i = 0; libs != null && i < libs.length; i++) {
            File jar = libs[i];
            xJarDecryptor.decrypt(key, jar, new File(enc, "BOOT-INF/lib/" + jar.getName()));
        }
        XKit.delete(lib, true);
        XDirDecryptor xDirDecryptor = new XDirDecryptor(xDecryptor);
        xDirDecryptor.decrypt(key, src, enc);
        // 压缩
        XKit.pack(enc, out);
    }
}
