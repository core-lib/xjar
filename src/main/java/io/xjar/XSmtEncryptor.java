package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 智能加密器
 *
 * @author Payne 646742615@qq.com
 * 2019/7/4 16:10
 */
public class XSmtEncryptor implements XEncryptor {

    @Override
    public void encrypt(XKey key, String src, String dest) throws IOException {
        new XJdkEncryptor(key.getAlgorithm()).encrypt(key, src, dest);
    }

    @Override
    public void encrypt(XKey key, File src, File dest) throws IOException {
        new XJdkEncryptor(key.getAlgorithm()).encrypt(key, src, dest);
    }

    @Override
    public void encrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        new XJdkEncryptor(key.getAlgorithm()).encrypt(key, in, out);
    }

    @Override
    public InputStream encrypt(XKey key, InputStream in) throws IOException {
        return new XJdkEncryptor(key.getAlgorithm()).encrypt(key, in);
    }

    @Override
    public OutputStream encrypt(XKey key, OutputStream out) throws IOException {
        return new XJdkEncryptor(key.getAlgorithm()).encrypt(key, out);
    }
}
