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
                .from("C:\\Users\\Payne\\IdeaProjects\\juniu-wxapp\\juniu-wxapp-web\\target\\juniu-wxapp-web-v1.0.0.jar")
                .use("io.xjar")
                .include("/cn/regent/juniu/wxapp/**/*")
                .to("C:\\Users\\Payne\\IdeaProjects\\juniu-wxapp\\juniu-wxapp-web\\target\\juniu-wxapp-web-v1.0.0.xjar");
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
