package io.xjar;

import io.xjar.key.SymmetricSecureKey;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;
import java.util.zip.Deflater;

/**
 * @author Payne 646742615@qq.com
 * 2018/11/22 14:19
 */
public class XJdkEncryptorTest implements XEntryFilter<JarArchiveEntry> {

    @Test
    public void test() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        XKey key = generate("AES", 128);
        String algorithm = "AES/CBC/PKCS7Padding";

        XEncryptor xEncryptor = new XJarEncryptor(new XJdkEncryptor(algorithm), Deflater.NO_COMPRESSION, this);
        XDecryptor xDecryptor = new XJarDecryptor(new XJdkDecryptor(algorithm), Deflater.NO_COMPRESSION, this);

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

    @Override
    public boolean filter(JarArchiveEntry entry) {
        return entry.getName().startsWith("BOOT-INF/classes/") || entry.getName().startsWith("BOOT-INF/lib/regent-service-");
    }
}
