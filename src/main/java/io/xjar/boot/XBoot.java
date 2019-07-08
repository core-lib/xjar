package io.xjar.boot;

import io.xjar.XKit;

/**
 * Spring-Boot JAR包加解密工具类，在不提供过滤器的情况下会加密BOOT-INF/下的所有资源，及包括项目本身的资源和依赖jar资源。
 *
 * @author Payne 646742615@qq.com
 * 2018/11/26 11:11
 */
public class XBoot extends XKit {

    /**
     * 获取加密器建造器
     *
     * @return 加密器建造器
     */
    public static XBootEncryptor.XBootEncryptorBuilder encryptor() {
        return XBootEncryptor.builder();
    }

    /**
     * 获取解密器建造器
     *
     * @return 解密器建造器
     */
    public static XBootDecryptor.XBootDecryptorBuilder decryptor() {
        return XBootDecryptor.builder();
    }
}
