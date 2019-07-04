package io.xjar;

import io.xjar.key.XKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/22 13:36
 */
public interface XEncryptor {

    /**
     * 加密，将目标文件加密输出至目标文件。
     *
     * @param key  密钥
     * @param src  源文件
     * @param dest 目标文件
     * @throws IOException I/O 异常
     */
    void encrypt(XKey key, String src, String dest) throws IOException;

    /**
     * 加密，将目标文件加密输出至目标文件。
     *
     * @param key  密钥
     * @param src  源文件
     * @param dest 目标文件
     * @throws IOException I/O 异常
     */
    void encrypt(XKey key, File src, File dest) throws IOException;

    /**
     * 加密，将输入流加密输出至输出流。
     *
     * @param key 密钥
     * @param in  输入流
     * @param out 输出流
     * @throws IOException I/O 异常
     */
    void encrypt(XKey key, InputStream in, OutputStream out) throws IOException;

    /**
     * 加密，将输入流包装成加密的输入流
     *
     * @param key 密钥
     * @param in  输入流
     * @return 加密后的输入流
     * @throws IOException I/O 异常
     */
    InputStream encrypt(XKey key, InputStream in) throws IOException;

    /**
     * 加密，将输入流包装成加密的输出流
     *
     * @param key 密钥
     * @param out 输出流
     * @return 加密后的输出流
     * @throws IOException I/O 异常
     */
    OutputStream encrypt(XKey key, OutputStream out) throws IOException;

}
