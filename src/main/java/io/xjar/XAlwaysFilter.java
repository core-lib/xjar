package io.xjar;

/**
 * 总是需要过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 21:58
 */
public class XAlwaysFilter<E> implements XEntryFilter<E> {

    @Override
    public boolean filter(E entry) {
        return true;
    }
}
