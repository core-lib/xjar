package io.xjar;

import io.xjar.key.XKey;

import java.io.*;

/**
 * 无操作解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 11:27
 */
public class XNopDecryptor implements XDecryptor {

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
        XKit.transfer(in, out);
    }

    @Override
    public InputStream decrypt(XKey key, InputStream in) throws IOException {
        return in;
    }

    @Override
    public OutputStream decrypt(XKey key, OutputStream out) throws IOException {
        return out;
    }
}
