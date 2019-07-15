package io.xjar;

import java.util.Arrays;
import java.util.Objects;

/**
 * 签名
 *
 * @author Payne 646742615@qq.com
 * 2019/7/15 10:35
 */
public class XSignature {
    private final String algorithm;
    private final byte[] signature;

    public XSignature(String algorithm, byte[] signature) {
        this.algorithm = algorithm;
        this.signature = signature;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public byte[] getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XSignature that = (XSignature) o;
        return Objects.equals(algorithm, that.algorithm) &&
                Arrays.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(algorithm);
        result = 31 * result + Arrays.hashCode(signature);
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(algorithm) + Arrays.toString(signature);
    }
}
