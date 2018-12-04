package io.xjar;

/**
 * 记录加/解密过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 20:19
 */
public interface XEntryFilter<E> {

    /**
     * 记录是否需要加/解密
     *
     * @param entry 记录
     * @return true: 需要 false:不需要
     */
    boolean filtrate(E entry);

}
