package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 智能解密器
 *
 * @author Payne 646742615@qq.com
 * 2019/7/4 16:10
 */
public class XSmtDecryptor implements XDecryptor {

    @Override
    public void decrypt(XKey key, String src, String dest) throws IOException {
        new XJdkDecryptor(key.getAlgorithm()).decrypt(key, src, dest);
    }

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        new XJdkDecryptor(key.getAlgorithm()).decrypt(key, src, dest);
    }

    @Override
    public void decrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        new XJdkDecryptor(key.getAlgorithm()).decrypt(key, in, out);
    }

    @Override
    public InputStream decrypt(XKey key, InputStream in) throws IOException {
        return new XJdkDecryptor(key.getAlgorithm()).decrypt(key, in);
    }

    @Override
    public OutputStream decrypt(XKey key, OutputStream out) throws IOException {
        return new XJdkDecryptor(key.getAlgorithm()).decrypt(key, out);
    }
}
