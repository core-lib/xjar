package io.xjar.key;

/**
 * 非对称密钥
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018-11-22 14:54:10
 */
public final class AsymmetricSecureKey extends SecureKey implements AsymmetricKey {
    private static final long serialVersionUID = -5120495228878483696L;

    private final byte[] publicKey;
    private final byte[] privateKey;

    public AsymmetricSecureKey(String algorithm, int size, byte[] publicKey, byte[] privateKey) {
        super(algorithm, size);
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public byte[] getEncryptKey() {
        return publicKey;
    }

    public byte[] getDecryptKey() {
        return privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public byte[] getIvParameter() {
        return null;
    }

}
