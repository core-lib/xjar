package io.xjar.digest;

import io.xjar.XDigest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * JDK内置摘要算法
 *
 * @author Payne 646742615@qq.com
 * 2019/7/3 22:27
 */
public class XJdkDigest implements XDigest {
    private final MessageDigest md;

    public XJdkDigest(String algorithm) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance(algorithm);
    }

    @Override
    public void digest(byte[] buf, int off, int len) {
        md.update(buf, off, len);
    }

    @Override
    public byte[] finish() {
        return md.digest();
    }

    @Override
    public void resume() {
        md.reset();
    }
}
