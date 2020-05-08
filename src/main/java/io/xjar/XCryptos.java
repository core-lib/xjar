package io.xjar;

import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.File;

/**
 * 智能加密/解密器
 *
 * @author Payne 646742615@qq.com
 * 2020/4/27 15:46
 */
public class XCryptos extends XFilters implements XConstants {

    /**
     * Jar 包加密
     *
     * @return 加密
     */
    public static XEncryption encryption() {
        return new XEncryption();
    }

    /**
     * Jar 包解密
     *
     * @return 解密
     */
    public static XDecryption decryption() {
        return new XDecryption();
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param xKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, XKey xKey) throws Exception {
        encrypt(new File(src), new File(dest), xKey);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param xKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, XKey xKey) throws Exception {
        XSmartEncryptor xSmartEncryptor = new XSmartEncryptor(new XJdkEncryptor());
        xSmartEncryptor.encrypt(xKey, src, dest);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(new File(src), new File(dest), xKey, filter);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        XSmartEncryptor xSmartEncryptor = new XSmartEncryptor(new XJdkEncryptor(), filter);
        xSmartEncryptor.encrypt(xKey, src, dest);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password) throws Exception {
        encrypt(src, dest, XKit.key(password));
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password) throws Exception {
        encrypt(src, dest, XKit.key(password));
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, XKit.key(password), filter);
    }

    /**
     * 加密 普通 JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, XKit.key(password), filter);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src  加密包
     * @param dest 解密包
     * @param xKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, XKey xKey) throws Exception {
        decrypt(new File(src), new File(dest), xKey);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src  加密包
     * @param dest 解密包
     * @param xKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, XKey xKey) throws Exception {
        XSmartDecryptor xSmartDecryptor = new XSmartDecryptor(new XJdkDecryptor());
        xSmartDecryptor.decrypt(xKey, src, dest);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src    加密包
     * @param dest   解密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(new File(src), new File(dest), xKey, filter);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src    加密包
     * @param dest   解密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        XSmartDecryptor xSmartDecryptor = new XSmartDecryptor(new XJdkDecryptor(), filter);
        xSmartDecryptor.decrypt(xKey, src, dest);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password) throws Exception {
        decrypt(src, dest, XKit.key(password));
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password) throws Exception {
        decrypt(src, dest, XKit.key(password));
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, XKit.key(password), filter);
    }

    /**
     * 解密 普通 JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, XKit.key(password), filter);
    }

}
