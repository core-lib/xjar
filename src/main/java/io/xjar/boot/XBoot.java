package io.xjar.boot;

import io.xjar.*;
import io.xjar.key.XKey;

import java.io.*;

/**
 * Spring-Boot JAR包加解密工具类，在不提供过滤器的情况下只加解密 BOOT-INF/classes/ 目录下的资源，也就是项目本身的资源。
 *
 * @author Payne 646742615@qq.com
 * 2018/11/26 11:11
 */
public class XBoot implements XConstants {

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filters  过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, XJarArchiveEntryFilter... filters) throws Exception {
        encrypt(src, dest, password, DEFAULT_ALGORITHM, filters);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, XJarArchiveEntryFilter... filters) throws Exception {
        encrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filters);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, int keysize, XJarArchiveEntryFilter... filters) throws Exception {
        encrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filters);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, int keysize, int ivsize, XJarArchiveEntryFilter... filters) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(in, out, password, algorithm, keysize, ivsize, filters);
        }
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in       原文包输入流
     * @param out      加密包输出流
     * @param password 密码
     * @param filters  过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, XJarArchiveEntryFilter... filters) throws Exception {
        encrypt(in, out, password, DEFAULT_ALGORITHM, filters);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, XJarArchiveEntryFilter... filters) throws Exception {
        encrypt(in, out, password, algorithm, DEFAULT_KEYSIZE, filters);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, XJarArchiveEntryFilter... filters) throws Exception {
        encrypt(in, out, password, algorithm, keysize, DEFAULT_IVSIZE, filters);
    }

    /**
     * 加密 Spring-Boot JAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, int ivsize, XJarArchiveEntryFilter... filters) throws Exception {
        XBootEncryptor xBootEncryptor = new XBootEncryptor(
                new XJdkEncryptor(algorithm),
                filters != null && filters.length > 0
                        ? filters
                        : new XJarArchiveEntryFilter[]{new XBootDefaultFilter()}
        );
        XKey xKey = XKit.key(algorithm, keysize, ivsize, password);
        xBootEncryptor.encrypt(xKey, in, out);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filters  过滤器
     * @throws Exception 加密异常
     */
    public static void decrypt(File src, File dest, String password, XJarArchiveEntryFilter... filters) throws Exception {
        decrypt(src, dest, password, DEFAULT_ALGORITHM, filters);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, XJarArchiveEntryFilter... filters) throws Exception {
        decrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filters);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, int keysize, XJarArchiveEntryFilter... filters) throws Exception {
        decrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filters);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, int keysize, int ivsize, XJarArchiveEntryFilter... filters) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            decrypt(in, out, password, algorithm, keysize, ivsize, filters);
        }
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in       加密包输入流
     * @param out      解密包输出流
     * @param password 密码
     * @param filters  过滤器
     * @throws Exception 加密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, XJarArchiveEntryFilter... filters) throws Exception {
        decrypt(in, out, password, DEFAULT_ALGORITHM, filters);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, XJarArchiveEntryFilter... filters) throws Exception {
        decrypt(in, out, password, algorithm, DEFAULT_KEYSIZE, filters);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, XJarArchiveEntryFilter... filters) throws Exception {
        decrypt(in, out, password, algorithm, keysize, DEFAULT_IVSIZE, filters);
    }

    /**
     * 解密 Spring-Boot JAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filters   过滤器
     * @throws Exception 加密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, int ivsize, XJarArchiveEntryFilter... filters) throws Exception {
        XBootDecryptor xBootDecryptor = new XBootDecryptor(
                new XJdkDecryptor(algorithm),
                filters != null && filters.length > 0
                        ? filters
                        : new XJarArchiveEntryFilter[]{new XBootDefaultFilter()}
        );
        XKey xKey = XKit.key(algorithm, keysize, ivsize, password);
        xBootDecryptor.decrypt(xKey, in, out);
    }

}
