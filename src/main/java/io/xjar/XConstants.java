package io.xjar;

/**
 * 常量表
 *
 * @author Payne 646742615@qq.com
 * 2018/11/24 9:17
 */
public interface XConstants {
    String BOOT_INF_CLASSES = "BOOT-INF/classes/";
    String BOOT_INF_LIB = "BOOT-INF/lib/";

    String WEB_INF_CLASSES = "WEB-INF/classes/";
    String WEB_INF_LIB = "WEB-INF/lib/";

    String META_INF_MANIFEST = "META-INF/MANIFEST.MF";
    String XJAR_SRC_DIR = XConstants.class.getPackage().getName().replace('.', '/') + "/";
    String XJAR_INF_DIR = "XJAR-INF/";
    String XJAR_INF_IDX = "INDEXES.IDX";
    String CRLF = System.getProperty("line.separator");

    String XJAR_ALGORITHM = "--xjar.algorithm=";
    String XJAR_KEYSIZE = "--xjar.keysize=";
    String XJAR_IVSIZE = "--xjar.ivsize=";
    String XJAR_PASSWORD = "--xjar.password=";
    String XJAR_KEYFILE = "--xjar.keyfile=";

    String XJAR_ALGORITHM_KEY = "XJar-Algorithm";
    String XJAR_KEYSIZE_KEY = "XJar-Keysize";
    String XJAR_IVSIZE_KEY = "XJar-Ivsize";
    String XJAR_PASSWORD_KEY = "XJar-Password";

    String XJAR_KEY_ALGORITHM = "algorithm";
    String XJAR_KEY_KEYSIZE = "keysize";
    String XJAR_KEY_IVSIZE = "ivsize";
    String XJAR_KEY_PASSWORD = "password";
    String XJAR_KEY_HOLD = "hold";

    String DEFAULT_ALGORITHM = "AES";
    int DEFAULT_KEYSIZE = 128;
    int DEFAULT_IVSIZE = 128;

    // 保留密钥在 META-INF/MANIFEST.MF 中，启动时无需输入密钥。
    int FLAG_DANGER = 1;
    // 危险模式：保留密钥
    int MODE_DANGER = FLAG_DANGER;
    // 普通模式
    int MODE_NORMAL = 0;

}
