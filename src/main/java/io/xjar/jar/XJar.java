package io.xjar.jar;

import io.xjar.XKit;

/**
 * 普通JAR包加解密工具类
 *
 * @author Payne 646742615@qq.com
 * 2018/11/26 11:11
 */
public class XJar extends XKit {

    /**
     * 获取加密器建造器
     *
     * @return 加密器建造器
     */
    public static XJarEncryptor.XJarEncryptorBuilder encryptor() {
        return new XJarEncryptor.XJarEncryptorBuilder();
    }

    /**
     * 获取解密器建造器
     *
     * @return 解密器建造器
     */
    public static XJarDecryptor.XJarDecryptorBuilder decryptor() {
        return new XJarDecryptor.XJarDecryptorBuilder();
    }

}
