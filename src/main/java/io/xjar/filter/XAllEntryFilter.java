package io.xjar.filter;

import io.xjar.XEntryFilter;

import java.util.Collection;

/**
 * ALL逻辑混合过滤器，即所有过滤器都满足的时候才满足，只要有一个过滤器不满足就立刻返回不满足，如果没有过滤器的时候则认为所有过滤器都满足。
 *
 * @author Payne 646742615@qq.com
 * 2018/12/4 15:26
 */
public class XAllEntryFilter<E> extends XMixEntryFilter<E> implements XEntryFilter<E> {

    public XAllEntryFilter() {
        super(null);
    }

    public XAllEntryFilter(Collection<? extends XEntryFilter<? extends E>> filters) {
        super(filters);
    }

    @Override
    public XAllEntryFilter<E> mix(XEntryFilter<? extends E> filter) {
        add(filter);
        return this;
    }

    @Override
    public boolean filtrate(E entry) {
        XEntryFilter[] filters = this.filters.toArray(new XEntryFilter[0]);
        for (XEntryFilter filter : filters) {
            if (!filter.filtrate(entry)) {
                return false;
            }
        }
        return true;
    }
}
