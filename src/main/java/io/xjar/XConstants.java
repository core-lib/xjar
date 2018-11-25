package io.xjar;

/**
 * 常量表
 *
 * @author Payne 646742615@qq.com
 * 2018/11/24 9:17
 */
public interface XConstants {
    String XJAR_INF_DIR = "XJAR-INF/";
    String XDEC_IDX_FILE = "XDEC.IDX";
    String XENC_IDX_FILE = "XENC.IDX";
    String CRLF = System.getProperty("line.separator");

    String XJAR_ALGORITHM = "--xjar.algorithm=";
    String XJAR_KEYSIZE = "--xjar.keysize=";
    String XJAR_PASSWORD = "--xjar.password=";

    String DEFAULT_ALGORITHM = "AES/CBC/PKCS7Padding";
    int DEFAULT_KEYSIZE = 128;

}
