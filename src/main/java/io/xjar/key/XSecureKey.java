package io.xjar.key;

import java.io.Serializable;

/**
 * 密钥
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018-11-22 14:54:10
 */
public abstract class XSecureKey implements XKey, Serializable {
    private static final long serialVersionUID = -5577962754674149355L;

    protected final String algorithm;
    protected final int keysize;
    protected final int ivsize;
    protected final String password;

    protected XSecureKey(String algorithm, int keysize, int ivsize, String password) {
        this.algorithm = algorithm;
        this.keysize = keysize;
        this.ivsize = ivsize;
        this.password = password;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public int getKeysize() {
        return keysize;
    }

    @Override
    public int getIvsize() {
        return ivsize;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
