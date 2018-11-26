package io.xjar;

/**
 * 常量表
 *
 * @author Payne 646742615@qq.com
 * 2018/11/24 9:17
 */
public interface XConstants {
    String META_INF_MANIFEST = "META-INF/MANIFEST.MF";
    String XJAR_SRC_DIR = XJar.class.getPackage().getName().replace('.', '/') + "/";
    String XJAR_INF_DIR = "XJAR-INF/";
    String XJAR_INF_IDX = "INDEXES.IDX";
    String CRLF = System.getProperty("line.separator");

    String XJAR_ALGORITHM = "--xjar.algorithm=";
    String XJAR_KEYSIZE = "--xjar.keysize=";
    String XJAR_IVSIZE = "--xjar.ivsize=";
    String XJAR_PASSWORD = "--xjar.password=";

    String DEFAULT_ALGORITHM = "AES";
    int DEFAULT_KEYSIZE = 128;
    int DEFAULT_IVSIZE = 128;

}
