package io.xjar.key;

/**
 * 对称密钥
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018-11-22 14:54:10
 */
public final class SymmetricSecureKey extends SecureKey implements SymmetricKey {
    private static final long serialVersionUID = -2932869368903909669L;

    private final byte[] secretKey;
    private final byte[] iv;

    public SymmetricSecureKey(String algorithm, int size, byte[] key, byte[] iv) {
        super(algorithm, size);
        this.secretKey = key;
        this.iv = iv;
    }

    public byte[] getEncryptKey() {
        return secretKey;
    }

    public byte[] getDecryptKey() {
        return secretKey;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public byte[] getIvParameter() {
        return iv;
    }

}
