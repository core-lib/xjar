package io.xjar;

import io.xjar.key.SymmetricSecureKey;
import io.xjar.key.XKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;

/**
 * @author Payne 646742615@qq.com
 * 2018/11/22 14:19
 */
public class XJdkEncryptorTest {

    @Test
    public void checksum() throws Exception {
        File file = new File("G:\\workspace\\regent-service\\regent-service-mr\\regent-service-mr-web\\target\\classes\\application.yml");
        FileInputStream fis = new FileInputStream(file);
        CheckedOutputStream cos = new CheckedOutputStream(System.out, new CRC32());
        XKit.transfer(fis, cos);
        System.out.println();
        System.out.println(file.length() + ":" + Integer.toHexString((int) cos.getChecksum().getValue()));
    }

    @Test
    public void test() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        XKey key = generate("AES", 128);
        String algorithm = "AES/CBC/PKCS7Padding";
        XEncryptor xEncryptor = new XJarEncryptor(new XJdkEncryptor(algorithm));
        XDecryptor xDecryptor = new XJarDecryptor(new XJdkDecryptor(algorithm));

        xEncryptor.encrypt(key, new File("D:\\xjar\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"), new File("D:\\xjar-encrypted\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"));
        xDecryptor.decrypt(key, new File("D:\\xjar-encrypted\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"), new File("D:\\xjar-decrypted\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"));
    }

    public XKey generate(String algorithm, int size) throws IOException {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(algorithm);
            generator.init(size, new SecureRandom());
            SecretKey key = generator.generateKey();
            byte[] iv = new byte[16];
            Random random = new Random();
            for (int i = 0; i < iv.length; i++) {
                iv[i] = (byte) (random.nextInt(Byte.MAX_VALUE) + 1);
            }
            return new SymmetricSecureKey(algorithm, size, key.getEncoded(), iv);
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }

}
