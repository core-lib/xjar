package io.xjar;

import io.xjar.key.XKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

/**
 * JDK内置解密算法的解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 14:01
 */
public class XJdkDecryptor implements XDecryptor {

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("could not make directory: " + dest.getParentFile());
        }
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            decrypt(key, in, out);
        }
    }

    @Override
    public void decrypt(XKey key, InputStream in, OutputStream out) throws IOException {
        CipherInputStream cis = null;
        try {
            String algorithm = key.getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm.split("[/]")[0]), new IvParameterSpec(key.getIvParameter()));
            cis = new CipherInputStream(in, cipher);
            XKit.transfer(cis, out);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            XKit.close(cis);
        }
    }

    @Override
    public InputStream decrypt(XKey key, InputStream in) throws IOException {
        try {
            String algorithm = key.getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm.split("[/]")[0]), new IvParameterSpec(key.getIvParameter()));
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public OutputStream decrypt(XKey key, OutputStream out) throws IOException {
        try {
            String algorithm = key.getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm.split("[/]")[0]), new IvParameterSpec(key.getIvParameter()));
            return new CipherOutputStream(out, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
