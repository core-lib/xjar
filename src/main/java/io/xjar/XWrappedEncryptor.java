package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 包装的加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 15:05
 */
public abstract class XWrappedEncryptor implements XEncryptor {
    protected final XEncryptor xEncryptor;

    protected XWrappedEncryptor(XEncryptor xEncryptor) {
        this.xEncryptor = xEncryptor;
    }

    @Override
    public void encrypt(XKey key, String src, String dest) throws IOException {
        xEncryptor.encrypt(key, src, dest);
    }

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        xEncryptor.encrypt(key, src, dest);
    }

    @Override
    public void encrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        xEncryptor.encrypt(key, in, out);
    }

    @Override
    public InputStream encrypt(XKey key, InputStream in) throws IOException {
        return xEncryptor.encrypt(key, in);
    }

    @Override
    public OutputStream encrypt(XKey key, OutputStream out) throws IOException {
        return xEncryptor.encrypt(key, out);
    }
}
