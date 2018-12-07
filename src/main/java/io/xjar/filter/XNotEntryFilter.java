package io.xjar.filter;

import io.xjar.XEntryFilter;

/**
 * 非门逻辑过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 12:21
 */
public class XNotEntryFilter<E> implements XEntryFilter<E> {
    private final XEntryFilter<E> delegate;

    public XNotEntryFilter(XEntryFilter<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean filtrate(E entry) {
        return !delegate.filtrate(entry);
    }
}
