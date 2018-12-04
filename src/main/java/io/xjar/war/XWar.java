package io.xjar.war;

import io.xjar.*;
import io.xjar.key.XKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.io.*;

/**
 * Java Web WAR包加解密工具类，目前WAR包加密还不能正常运行！
 *
 * @author Payne 646742615@qq.com
 * 2018/11/26 11:11
 */
public class XWar implements XConstants {

    /**
     * 加密Java Web WAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filter  过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm, int keysize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(String src, String dest, String password, String algorithm, int keysize, int ivsize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(new File(src), new File(dest), password, algorithm, keysize, ivsize, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param src      原文包
     * @param dest     加密包
     * @param password 密码
     * @param filter  过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, int keysize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param src       原文包
     * @param dest      加密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(File src, File dest, String password, String algorithm, int keysize, int ivsize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            encrypt(in, out, password, algorithm, keysize, ivsize, filter);
        }
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param in       原文包输入流
     * @param out      加密包输出流
     * @param password 密码
     * @param filter  过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(in, out, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(in, out, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        encrypt(in, out, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 加密Java Web WAR 包
     *
     * @param in        原文包输入流
     * @param out       加密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter   过滤器
     * @throws Exception 加密异常
     */
    public static void encrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, int ivsize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        XWarEncryptor xJarEncryptor = new XWarEncryptor(new XJdkEncryptor(algorithm), filter);
        XKey xKey = XKit.key(algorithm, keysize, ivsize, password);
        xJarEncryptor.encrypt(xKey, in, out);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filter  过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm, int keysize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(String src, String dest, String password, String algorithm, int keysize, int ivsize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(new File(src), new File(dest), password, algorithm, keysize, ivsize, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param src      加密包
     * @param dest     解密包
     * @param password 密码
     * @param filter  过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, int keysize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(src, dest, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param src       加密包
     * @param dest      解密包
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(File src, File dest, String password, String algorithm, int keysize, int ivsize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest)
        ) {
            decrypt(in, out, password, algorithm, keysize, ivsize, filter);
        }
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param in       加密包输入流
     * @param out      解密包输出流
     * @param password 密码
     * @param filter  过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(in, out, password, DEFAULT_ALGORITHM, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(in, out, password, algorithm, DEFAULT_KEYSIZE, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        decrypt(in, out, password, algorithm, keysize, DEFAULT_IVSIZE, filter);
    }

    /**
     * 解密Java Web WAR 包
     *
     * @param in        加密包输入流
     * @param out       解密包输出流
     * @param password  密码
     * @param algorithm 加密算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param filter   过滤器
     * @throws Exception 解密异常
     */
    public static void decrypt(InputStream in, OutputStream out, String password, String algorithm, int keysize, int ivsize, XEntryFilter<JarArchiveEntry> filter) throws Exception {
        XWarDecryptor xJarDecryptor = new XWarDecryptor(new XJdkDecryptor(algorithm), filter);
        XKey xKey = XKit.key(algorithm, keysize, ivsize, password);
        xJarDecryptor.decrypt(xKey, in, out);
    }

}
