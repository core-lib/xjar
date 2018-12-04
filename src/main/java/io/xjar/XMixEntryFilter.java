package io.xjar;

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
    protected final Set<XEntryFilter<E>> filters;

    protected XMixEntryFilter() {
        this(null);
    }

    protected XMixEntryFilter(Collection<? extends XEntryFilter<E>> filters) {
        this.filters = filters != null ? new LinkedHashSet<>(filters) : new LinkedHashSet<XEntryFilter<E>>();
    }

    public boolean add(XEntryFilter<E> filter) {
        return filters.add(filter);
    }

    public boolean remove(XEntryFilter<E> filter) {
        return filters.remove(filter);
    }

    public abstract XMixEntryFilter<E> mix(XEntryFilter<E> filter);
}
