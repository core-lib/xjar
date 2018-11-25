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

        XKey xKey = XKit.key("Payne");

        XEncryptor xEncryptor = new XJarEncryptor(new XJdkEncryptor(DEFAULT_ALGORITHM), Deflater.NO_COMPRESSION, this);
        XDecryptor xDecryptor = new XJarDecryptor(new XJdkDecryptor(DEFAULT_ALGORITHM), Deflater.NO_COMPRESSION, this);

        xEncryptor.encrypt(xKey, new File("G:\\xjar\\xjar-test-1.0-SNAPSHOT.jar"), new File("G:\\xjar-encrypted\\xjar-test-1.0-SNAPSHOT.jar"));
        xDecryptor.decrypt(xKey, new File("G:\\xjar-encrypted\\xjar-test-1.0-SNAPSHOT.jar"), new File("G:\\xjar-decrypted\\xjar-test-1.0-SNAPSHOT.jar"));
    }

    @Override
    public boolean filter(JarArchiveEntry entry) {
        return true;
    }
}
