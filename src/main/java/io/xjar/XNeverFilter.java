package io.xjar;

/**
 * 总不需要过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 21:59
 */
public class XNeverFilter<E> implements XEntryFilter<E> {
    @Override
    public boolean filter(E entry) {
        return false;
    }
}
