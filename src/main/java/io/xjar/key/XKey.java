package io.xjar.key;

/**
 * 密钥
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018-11-22 14:54:10
 */
public interface XKey {

    /**
     * @return 密钥算法名称
     */
    String getAlgorithm();

    /**
     * @return 密钥长度
     */
    int getKeysize();

    /**
     * @return 向量长度
     */
    int getIvsize();

    /**
     * @return 密码
     */
    String getPassword();

    /**
     * @return 加密密钥
     */
    byte[] getEncryptKey();

    /**
     * @return 解密密钥
     */
    byte[] getDecryptKey();

    /**
     * @return 向量参数
     */
    byte[] getIvParameter();

}