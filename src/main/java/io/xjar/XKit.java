package io.xjar;

import io.xjar.filter.XAllEntryFilter;
import io.xjar.filter.XAnyEntryFilter;
import io.xjar.filter.XNotEntryFilter;
import io.xjar.key.XKey;
import io.xjar.key.XSecureRandom;
import io.xjar.key.XSymmetricSecureKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * XJar 工具类，包含I/O，密钥，过滤器的工具方法。
 */
public abstract class XKit implements XConstants {

    /**
     * 从输入流中读取一行字节码
     *
     * @param in 输入流
     * @return 最前面的一行字节码
     * @throws IOException I/O 异常
     */
    public static byte[] readln(InputStream in) throws IOException {
        int b = in.read();
        if (b == -1) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (b != -1) {
            switch (b) {
                case '\r':
                    break;
                case '\n':
                    return bos.toByteArray();
                default:
                    bos.write(b);
                    break;
            }
            b = in.read();
        }
        return bos.toByteArray();
    }

    /**
     * 往输出流中写入一行字节码
     *
     * @param out  输出流
     * @param line 一行字节码
     * @throws IOException I/O 异常
     */
    public static void writeln(OutputStream out, byte[] line) throws IOException {
        if (line == null) {
            return;
        }
        out.write(line);
        out.write('\r');
        out.write('\n');
    }

    public static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        transfer(in, bos);
        return bos.toByteArray();
    }

    public static void write(OutputStream out, byte[] data) throws IOException {
        if (data == null) {
            return;
        }
        out.write(data);
    }

    /**
     * 关闭资源，等效于XKit.close(closeable, true);
     *
     * @param closeable 资源
     */
    public static void close(Closeable closeable) {
        try {
            close(closeable, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭资源
     *
     * @param closeable 资源
     * @param quietly   是否安静关闭，即捕获到关闭异常时是否忽略
     * @throws IOException 当quietly == false, 时捕获到的I/O异常将会往外抛
     */
    public static void close(Closeable closeable, boolean quietly) throws IOException {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            if (!quietly) throw e;
        }
    }

    /**
     * 输入流传输到输出流
     *
     * @param in  输入流
     * @param out 输出流
     * @return 传输长度
     * @throws IOException I/O 异常
     */
    public static long transfer(InputStream in, OutputStream out) throws IOException {
        long total = 0;
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
            total += length;
        }
        out.flush();
        return total;
    }

    /**
     * reader传输到writer
     *
     * @param reader reader
     * @param writer writer
     * @return 传输长度
     * @throws IOException I/O 异常
     */
    public static long transfer(Reader reader, Writer writer) throws IOException {
        long total = 0;
        char[] buffer = new char[4096];
        int length;
        while ((length = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, length);
            total += length;
        }
        writer.flush();
        return total;
    }

    /**
     * 输入流传输到文件
     *
     * @param in   输入流
     * @param file 文件
     * @return 传输长度
     * @throws IOException I/O 异常
     */
    public static long transfer(InputStream in, File file) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            return transfer(in, out);
        } finally {
            close(out);
        }
    }

    /**
     * reader传输到文件
     *
     * @param reader reader
     * @param file   文件
     * @return 传输长度
     * @throws IOException I/O 异常
     */
    public static long transfer(Reader reader, File file) throws IOException {
        OutputStream out = null;
        Writer writer = null;
        try {
            out = new FileOutputStream(file);
            writer = new OutputStreamWriter(out);
            return transfer(reader, writer);
        } finally {
            close(writer);
            close(out);
        }
    }

    /**
     * 删除文件，如果是目录将不递归删除子文件或目录，等效于delete(file, false);
     *
     * @param file 文件/目录
     * @return 是否删除成功
     */
    public static boolean delete(File file) {
        return delete(file, false);
    }

    /**
     * 删除文件，如果是目录将递归删除子文件或目录
     *
     * @param file 文件/目录
     * @return 是否删除成功
     */
    public static boolean delete(File file, boolean recursively) {
        if (file.isDirectory() && recursively) {
            boolean deleted = true;
            File[] files = file.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                deleted &= delete(files[i], true);
            }
            return deleted && file.delete();
        } else {
            return file.delete();
        }
    }

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
     * @param keysize   密钥长度
     * @param ivsize    向量长度
     * @param password  密码
     * @return 密钥
     * @throws NoSuchAlgorithmException 没有该密钥算法
     */
    public static XKey key(String algorithm, int keysize, int ivsize, String password) throws NoSuchAlgorithmException {
        MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        byte[] seed = sha512.digest(password.getBytes(StandardCharsets.UTF_8));
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
    public static <E> XAllEntryFilter<E> all() {
        return new XAllEntryFilter<>();
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static <E> XAllEntryFilter<E> all(Collection<? extends XEntryFilter<E>> filters) {
        return new XAllEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static <E> XAllEntryFilter<E> and() {
        return new XAllEntryFilter<>();
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static <E> XAllEntryFilter<E> and(Collection<? extends XEntryFilter<E>> filters) {
        return new XAllEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static <E> XAnyEntryFilter<E> any() {
        return new XAnyEntryFilter<>();
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static <E> XAnyEntryFilter<E> any(Collection<? extends XEntryFilter<E>> filters) {
        return new XAnyEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static <E> XAnyEntryFilter<E> or() {
        return new XAnyEntryFilter<>();
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static <E> XAnyEntryFilter<E> or(Collection<? extends XEntryFilter<E>> filters) {
        return new XAnyEntryFilter<>(filters);
    }

    /**
     * 创建非门逻辑运算过滤器，实际上就是将委派过滤器的过滤结果取反
     *
     * @param filter 委派过滤器
     * @param <E>    记录类型
     * @return 非门逻辑过滤器
     */
    public static <E> XEntryFilter<E> not(XEntryFilter<E> filter) {
        return new XNotEntryFilter<>(filter);
    }

    public static boolean isRelative(String path) {
        return !isAbsolute(path);
    }

    public static boolean isAbsolute(String path) {
        if (path.startsWith("/")) {
            return true;
        }
        Set<File> roots = new HashSet<>();
        Collections.addAll(roots, File.listRoots());
        File root = new File(path);
        while (root.getParentFile() != null) {
            root = root.getParentFile();
        }
        return roots.contains(root);
    }

    public static String absolutize(String path) {
        return normalize(isAbsolute(path) ? path : System.getProperty("user.dir") + File.separator + path);
    }

    public static String normalize(String path) {
        return path.replaceAll("[/\\\\]+", "/");
    }

    public static byte[] md5(File file) throws IOException {
        try {
            return hash(file, MessageDigest.getInstance("MD5"));
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }

    public static byte[] sha1(File file) throws IOException {
        try {
            return hash(file, MessageDigest.getInstance("SHA-1"));
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }

    public static byte[] hash(File file, MessageDigest hash) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(file)
        ) {
            byte[] buf = new byte[8 * 1024];
            int len;
            while ((len = fis.read(buf)) != -1) {
                hash.update(buf, 0, len);
            }
            return hash.digest();
        }
    }

}
