package io.xjar.demo;

import io.xjar.XCryptos;
import org.junit.Test;

/**
 * XJar Demo
 *
 * @author Payne 646742615@qq.com
 * 2020/5/7 14:31
 */
public class XDemo {

    @Test
    public void testEncrypt() throws Exception {
        XCryptos.encryption()
                .from("/path/to/read/plaintext.jar")
                .use("password")
                .include("/package/name/**/*.class")
                .include("/mapper/**/*.xml")
                .exclude("/static/**/*")
                .exclude("/conf/*")
                .to("/path/to/save/encrypted.jar");
    }

    @Test
    public void testDecrypt() throws Exception {
        XCryptos.decryption()
                .from("/path/to/read/encrypted.jar")
                .use("password")
                .include("/package/name/**/*.class")
                .include("/mapper/**/*.xml")
                .exclude("/static/**/*")
                .exclude("/conf/*")
                .to("/path/to/save/plaintext.jar");
    }

}
