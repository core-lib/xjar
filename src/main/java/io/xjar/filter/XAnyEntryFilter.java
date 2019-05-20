package io.xjar.filter;

import io.xjar.XEntryFilter;

import java.util.Collection;

/**
 * ANY逻辑混合过滤器，即任意一个过滤器满足时就满足，当没有过滤器的时候则认为没有过滤器满足，也就是不满足。
 *
 * @author Payne 646742615@qq.com
 * 2018/12/4 15:26
 */
public class XAnyEntryFilter<E> extends XMixEntryFilter<E> implements XEntryFilter<E> {

    public XAnyEntryFilter() {
        super(null);
    }

    public XAnyEntryFilter(Collection<? extends XEntryFilter<? extends E>> filters) {
        super(filters);
    }

    @Override
    public XAnyEntryFilter<E> mix(XEntryFilter<? extends E> filter) {
        add(filter);
        return this;
    }

    @Override
    public boolean filtrate(E entry) {
        XEntryFilter[] filters = this.filters.toArray(new XEntryFilter[0]);
        for (XEntryFilter filter : filters) {
            if (filter.filtrate(entry)) {
                return true;
            }
        }
        return false;
    }
}
