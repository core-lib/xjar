package io.xjar.boot;

import io.xjar.*;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Spring-Boot JAR包加解密工具类，在不提供过滤器的情况下会加密BOOT-INF/下的所有资源，及包括项目本身的资源和依赖jar资源。
 *
 * @author Payne 646742615@qq.com
 * 2018/11/26 11:11
 */
public class XBoot extends XFilters implements XConstants {

    /**
     * 加密 Spring-Boot JAR 包
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
     * 加密 Spring-Boot JAR 包
     *
     * @param src  原文包
     * @param dest 加密包
     * @param xKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, XKey xKey) throws Exception {
        XBootEncryptor xBootEncryptor = new XBootEncryptor(new XJdkEncryptor());
        xBootEncryptor.encrypt(xKey, src, dest);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in   原文包输入流
     * @param out  加密包输出流
     * @param xKey 密钥
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, XKey xKey) throws Exception {
        XBootEncryptor xBootEncryptor = new XBootEncryptor(new XJdkEncryptor());
        xBootEncryptor.encrypt(xKey, in, out);
    }

    /**
     * 加密 Spring-Boot JAR 包
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
     * 加密 Spring-Boot JAR 包
     *
     * @param src    原文包
     * @param dest   加密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        XBootEncryptor xBootEncryptor = new XBootEncryptor(new XJdkEncryptor(), filter);
        xBootEncryptor.encrypt(xKey, src, dest);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in     原文包输入流
     * @param out    加密包输出流
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        XBootEncryptor xBootEncryptor = new XBootEncryptor(new XJdkEncryptor(), filter);
        xBootEncryptor.encrypt(xKey, in, out);
    }

    /**
     * 加密 Spring-Boot JAR 包
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
     * 加密 Spring-Boot JAR 包
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
     * 加密 Spring-Boot JAR 包
     *
     * @param in       原文包输入流
     * @param out      加密包输出流
     * @param password 密码
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password) throws Exception {
        encrypt(in, out, XKit.key(password));
    }

    /**
     * 加密 Spring-Boot JAR 包
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
     * 加密 Spring-Boot JAR 包
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
     * 加密 Spring-Boot JAR 包
     *
     * @param in       原文包输入流
     * @param out      加密包输出流
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(in, out, XKit.key(password), filter);
    }

    /**
     * 解密 Spring-Boot JAR 包
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
     * 解密 Spring-Boot JAR 包
     *
     * @param src  加密包
     * @param dest 解密包
     * @param xKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, XKey xKey) throws Exception {
        XBootDecryptor xBootDecryptor = new XBootDecryptor(new XJdkDecryptor());
        xBootDecryptor.decrypt(xKey, src, dest);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in   加密包输入流
     * @param out  解密包输出流
     * @param xKey 密钥
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, XKey xKey) throws Exception {
        XBootDecryptor xBootDecryptor = new XBootDecryptor(new XJdkDecryptor());
        xBootDecryptor.decrypt(xKey, in, out);
    }

    /**
     * 解密 Spring-Boot JAR 包
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
     * 解密 Spring-Boot JAR 包
     *
     * @param src    加密包
     * @param dest   解密包
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        XBootDecryptor xBootDecryptor = new XBootDecryptor(new XJdkDecryptor(), filter);
        xBootDecryptor.decrypt(xKey, src, dest);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in     加密包输入流
     * @param out    解密包输出流
     * @param xKey   密钥
     * @param filter 过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, XKey xKey, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        XBootDecryptor xBootDecryptor = new XBootDecryptor(new XJdkDecryptor(), filter);
        xBootDecryptor.decrypt(xKey, in, out);
    }

    /**
     * 解密 Spring-Boot JAR 包
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
     * 解密 Spring-Boot JAR 包
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
     * 解密 Spring-Boot JAR 包
     *
     * @param in       加密包输入流
     * @param out      解密包输出流
     * @param password 密码
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password) throws Exception {
        decrypt(in, out, XKit.key(password));
    }

    /**
     * 解密 Spring-Boot JAR 包
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
     * 解密 Spring-Boot JAR 包
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

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in       加密包输入流
     * @param out      解密包输出流
     * @param password 密码
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(in, out, XKit.key(password), filter);
    }

}
