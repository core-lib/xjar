package io.xjar;

public class XJar {

    public static void main(String[] args) throws Exception {
        String sourceJar = System.getProperty("xjar.source");
        if (stringIsEmpty(sourceJar)) {
            System.err.println("请使用-Dxjar.source系统属性指定要加密的jar包。");
            return;
        }
        String destJar = System.getProperty("xjar.dest");
        if (stringIsEmpty(destJar)) {
            System.err.println("请使用-Dxjar.dest系统属性指定加密后的jar包。");
            return;
        }
        String password = System.getProperty("xjar.password");
        if (stringIsEmpty(password)) {
            System.err.println("请使用-Dxjar.password系统属性指定加密秘钥，长度小于256字节。");
            return;
        }
        String algorithm = System.getProperty("xjar.algorithm");
        String keysize = System.getProperty("xjar.keysize");
        String ivsize = System.getProperty("xjar.ivsize");
        String include = System.getProperty("xjar.include");
        String exclude = System.getProperty("xjar.exclude");
        XEncryption encryption = XCryptos.encryption().from(sourceJar);
        if (stringIsEmpty(algorithm) || stringIsEmpty(keysize) || stringIsEmpty(ivsize)) {
            encryption = encryption.use(password);
        }
        else {
            encryption = encryption.use(algorithm, Integer.parseInt(keysize), Integer.parseInt(ivsize), password);
        }
        if (!stringIsEmpty(include)) {
            encryption = encryption.include(include);
        }
        if (!stringIsEmpty(exclude)) {
            encryption = encryption.exclude(exclude);
        }
        System.out.println("正在进行加密操作，请稍候...");
        encryption.to(destJar);
    }
    
    private static boolean stringIsEmpty(String str) {
        return str == null || str.trim().length() <= 0;
    }

}
