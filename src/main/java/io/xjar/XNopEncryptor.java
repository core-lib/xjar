package io.xjar;

import io.xjar.key.XKey;

import java.io.*;

/**
 * 无操作加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 11:27
 */
public class XNopEncryptor implements XEncryptor {

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
        XKit.transfer(in, out);
    }

    @Override
    public InputStream encrypt(XKey key, InputStream in) throws IOException {
        return in;
    }

    @Override
    public OutputStream encrypt(XKey key, OutputStream out) throws IOException {
        return out;
    }
}
