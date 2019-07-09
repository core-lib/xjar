package io.xjar.digest;

import io.xjar.XDigest;
import io.xjar.XDigestFactory;

import java.security.NoSuchAlgorithmException;

/**
 * JDK内置摘要算法对象工厂
 *
 * @author Payne 646742615@qq.com
 * 2019/7/5 10:22
 */
public class XJdkDigestFactory extends XRecycledDigestFactory implements XDigestFactory {

    @Override
    protected XDigest produce(String algorithm) throws NoSuchAlgorithmException {
        return new XJdkDigest(algorithm);
    }
}
