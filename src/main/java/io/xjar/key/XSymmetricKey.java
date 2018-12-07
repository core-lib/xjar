package io.xjar.key;

/**
 * 对称密钥
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018-11-22 14:54:10
 */
public interface XSymmetricKey extends XKey {

    /**
     * @return 密钥
     */
    byte[] getSecretKey();

}
