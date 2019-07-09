package io.xjar.digest;

import io.xjar.XDigest;
import io.xjar.XDigestFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * 重复利用的摘要算法对象工厂
 *
 * @author Payne 646742615@qq.com
 * 2019/7/9 10:50
 */
public abstract class XRecycledDigestFactory implements XDigestFactory {
    private final ConcurrentMap<String, Queue<XDigest>> pool = new ConcurrentHashMap<>();

    /**
     * 生产一个新的摘要算法对象
     *
     * @param algorithm 摘要算法
     * @return 摘要算法对象
     * @throws NoSuchAlgorithmException 摘要算法不支持
     */
    protected abstract XDigest produce(String algorithm) throws NoSuchAlgorithmException;

    @Override
    public XDigest acquire(String algorithm) throws NoSuchAlgorithmException {
        Queue<XDigest> queue = pool.get(algorithm);
        XDigest digest = queue != null ? queue.poll() : null;
        if (digest == null) {
            digest = produce(algorithm);
        }
        digest.resume();
        return digest;
    }

    @Override
    public void release(String algorithm, XDigest digest) {
        digest.resume();
        Queue<XDigest> queue = new ConcurrentLinkedQueue<>();
        queue.offer(digest);
        Queue<XDigest> old = pool.putIfAbsent(algorithm, queue);
        if (old != null) {
            old.offer(digest);
        }
    }
}
