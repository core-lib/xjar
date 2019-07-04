package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 包装的解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:22
 */
public abstract class XWrappedDecryptor implements XDecryptor {
    protected final XDecryptor xDecryptor;

    protected XWrappedDecryptor(XDecryptor xDecryptor) {
        this.xDecryptor = xDecryptor;
    }

    @Override
    public void decrypt(XKey key, String src, String dest) throws IOException {
        xDecryptor.decrypt(key, src, dest);
    }

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        xDecryptor.decrypt(key, src, dest);
    }

    @Override
    public void decrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        xDecryptor.decrypt(key, in, out);
    }

    @Override
    public InputStream decrypt(XKey key, InputStream in) throws IOException {
        return xDecryptor.decrypt(key, in);
    }

    @Override
    public OutputStream decrypt(XKey key, OutputStream out) throws IOException {
        return xDecryptor.decrypt(key, out);
    }
}
