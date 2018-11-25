package io.xjar;

import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.io.File;
import java.security.Security;
import java.util.zip.Deflater;

/**
 * @author Payne 646742615@qq.com
 * 2018/11/22 14:19
 */
public class XJdkEncryptorTest implements XEntryFilter<JarArchiveEntry>, XConstants {

    @Test
    public void test() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        XKey xKey = XKit.generate(DEFAULT_ALGORITHM, DEFAULT_KEYSIZE, "Payne");

        XEncryptor xEncryptor = new XJarEncryptor(new XJdkEncryptor(DEFAULT_ALGORITHM), Deflater.NO_COMPRESSION, this);
        XDecryptor xDecryptor = new XJarDecryptor(new XJdkDecryptor(DEFAULT_ALGORITHM), Deflater.NO_COMPRESSION, this);

        xEncryptor.encrypt(xKey, new File("D:\\xjar\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"), new File("D:\\xjar-encrypted\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"));
        xDecryptor.decrypt(xKey, new File("D:\\xjar-encrypted\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"), new File("D:\\xjar-decrypted\\regent-service-mr-web-0.0.1-SNAPSHOT.jar"));
    }

    @Override
    public boolean filter(JarArchiveEntry entry) {
        return entry.getName().startsWith("BOOT-INF/classes/") || entry.getName().startsWith("BOOT-INF/lib/regent-service-");
    }
}
