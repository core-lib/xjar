package io.xjar;

import io.xjar.key.XKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

/**
 * JDK内置解密算法的解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 14:01
 */
public class XJdkDecryptor implements XDecryptor {
    private final String algorithm;

    public XJdkDecryptor(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void decrypt(XKey key, String src, String dest) throws IOException {
        decrypt(key, new File(src), new File(dest));
    }

    @Override
    public void decrypt(XKey key, File src, File dest) throws IOException {
        File dir = dest.getParentFile();
        if (!dir.exists() && !dir.mkdirs() && !dir.exists()) {
            throw new IOException("could not make directory: " + dir);
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
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm));
            cis = new CipherInputStream(in, cipher);
            XTool.transfer(cis, out);
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            XTool.close(cis);
        }
    }

    @Override
    public InputStream decrypt(XKey key, InputStream in) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm));
            return new CipherInputStream(in, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public OutputStream decrypt(XKey key, OutputStream out) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getDecryptKey(), algorithm));
            return new CipherOutputStream(out, cipher);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
