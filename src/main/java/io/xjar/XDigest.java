package io.xjar;

/**
 * 摘要算法
 *
 * @author Payne 646742615@qq.com
 * 2019/7/3 22:25
 */
public interface XDigest {

    void digest(byte[] buf, int off, int len);

    byte[] finish();

    void resume();

}
