package io.xjar;

/**
 * 摘要算法
 *
 * @author Payne 646742615@qq.com
 * 2019/7/3 22:25
 */
public interface XDigest {

    /**
     * 计算数据摘要
     *
     * @param buf 缓冲
     * @param off 下标
     * @param len 长度
     */
    void digest(byte[] buf, int off, int len);

    /**
     * 结束计算并返回摘要值
     *
     * @return 最终摘要值
     */
    byte[] finish();

    /**
     * 恢复初始状态
     */
    void resume();

}
