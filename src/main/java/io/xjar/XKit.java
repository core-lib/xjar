package io.xjar;

import io.xjar.filter.*;
import io.xjar.jar.XJarAntEntryFilter;
import io.xjar.jar.XJarRegexEntryFilter;
import io.xjar.key.XKey;
import io.xjar.key.XSecureRandom;
import io.xjar.key.XSymmetricSecureKey;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * 工具类
 *
 * @author Payne 646742615@qq.com
 * 2019/7/8 15:52
 */
public abstract class XKit implements XConstants {

    /**
     * 根据密码生成密钥
     *
     * @param password 密码
     * @return 密钥
     * @throws NoSuchAlgorithmException 没有该密钥算法
     */
    public static XKey key(String password) throws NoSuchAlgorithmException {
        return key(DEFAULT_ALGORITHM, DEFAULT_KEYSIZE, DEFAULT_IVSIZE, password);
    }

    /**
     * 根据密码生成密钥
     *
     * @param algorithm 密钥算法
     * @param password  密码
     * @return 密钥
     * @throws NoSuchAlgorithmException 没有该密钥算法
     */
    public static XKey key(String algorithm, String password) throws NoSuchAlgorithmException {
        return key(algorithm, DEFAULT_KEYSIZE, DEFAULT_IVSIZE, password);
    }

    /**
     * 根据密码生成密钥
     *
     * @param algorithm 密钥算法
     * @param keysize   密钥长度
     * @param password  密码
     * @return 密钥
     * @throws NoSuchAlgorithmException 没有该密钥算法
     */
    public static XKey key(String algorithm, int keysize, String password) throws NoSuchAlgorithmException {
        return key(algorithm, keysize, DEFAULT_IVSIZE, password);
    }

    /**
     * 根据密码生成密钥
     *
     * @param algorithm 密钥算法
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param password  密码
     * @return 密钥
     * @throws NoSuchAlgorithmException 没有该密钥算法
     */
    public static XKey key(String algorithm, int keysize, int ivsize, String password) throws NoSuchAlgorithmException {
        MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        byte[] seed = sha512.digest(password.getBytes());
        KeyGenerator generator = KeyGenerator.getInstance(algorithm.split("[/]")[0]);
        XSecureRandom random = new XSecureRandom(seed);
        generator.init(keysize, random);
        SecretKey key = generator.generateKey();
        generator.init(ivsize, random);
        SecretKey iv = generator.generateKey();
        return new XSymmetricSecureKey(algorithm, keysize, ivsize, password, key.getEncoded(), iv.getEncoded());
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static XAllEntryFilter<JarArchiveEntry> all() {
        return new XAllEntryFilter<>();
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static XAllEntryFilter<JarArchiveEntry> all(Collection<? extends XEntryFilter<JarArchiveEntry>> filters) {
        return new XAllEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static XAllEntryFilter<JarArchiveEntry> and() {
        return new XAllEntryFilter<>();
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static XAllEntryFilter<JarArchiveEntry> and(Collection<? extends XEntryFilter<JarArchiveEntry>> filters) {
        return new XAllEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static XAnyEntryFilter<JarArchiveEntry> any() {
        return new XAnyEntryFilter<>();
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static XAnyEntryFilter<JarArchiveEntry> any(Collection<? extends XEntryFilter<JarArchiveEntry>> filters) {
        return new XAnyEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static XAnyEntryFilter<JarArchiveEntry> or() {
        return new XAnyEntryFilter<>();
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static XAnyEntryFilter<JarArchiveEntry> or(Collection<? extends XEntryFilter<JarArchiveEntry>> filters) {
        return new XAnyEntryFilter<>(filters);
    }

    /**
     * 创建非门逻辑运算过滤器，实际上就是将委派过滤器的过滤结果取反
     *
     * @param filter 委派过滤器
     * @return 非门逻辑过滤器
     */
    public static XEntryFilter<JarArchiveEntry> not(XEntryFilter<JarArchiveEntry> filter) {
        return new XNotEntryFilter<>(filter);
    }

    /**
     * 创建JAR ANT 表达式过滤器
     *
     * @param ant ANT 表达式
     * @return JAR ANT 表达式过滤器
     */
    public static XAntEntryFilter<JarArchiveEntry> ant(String ant) {
        return new XJarAntEntryFilter(ant);
    }

    /**
     * 创建JAR 正则表达式过滤器
     *
     * @param regex 正则表达式
     * @return JAR 正则表达式过滤器
     */
    public static XRegexEntryFilter<JarArchiveEntry> regex(String regex) {
        return new XJarRegexEntryFilter(regex);
    }

}
