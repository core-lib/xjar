package io.xjar.filter;

import io.xjar.XEntryFilter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 混合过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/4 15:20
 */
public abstract class XMixEntryFilter<E> implements XEntryFilter<E> {
    protected final Set<XEntryFilter<? extends E>> filters;

    protected XMixEntryFilter() {
        this(null);
    }

    protected XMixEntryFilter(Collection<? extends XEntryFilter<? extends E>> filters) {
        this.filters = filters != null ? new LinkedHashSet<>(filters) : new LinkedHashSet<XEntryFilter<? extends E>>();
    }

    public boolean add(XEntryFilter<? extends E> filter) {
        return filters.add(filter);
    }

    public boolean remove(XEntryFilter<? extends E> filter) {
        return filters.remove(filter);
    }

    public int size() {
        return filters.size();
    }

    public abstract XMixEntryFilter<E> mix(XEntryFilter<? extends E> filter);
}
