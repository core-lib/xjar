package io.xjar;

import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法对象工厂
 *
 * @author Payne 646742615@qq.com
 * 2019/7/5 10:16
 */
public interface XDigestFactory {

    /**
     * 生产摘要算法对象
     *
     * @param algorithm 摘要算法
     * @return 摘要算法对象
     * @throws NoSuchAlgorithmException 摘要算法不支持
     */
    XDigest acquire(String algorithm) throws NoSuchAlgorithmException;

    /**
     * 回收摘要算法对象
     *
     * @param algorithm 摘要算法
     * @param digest    摘要算法对象
     */
    void release(String algorithm, XDigest digest);

}
