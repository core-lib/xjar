package io.xjar;

import java.util.Collection;
import java.util.Collections;

/**
 * 记录可过滤的加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 20:38
 */
public abstract class XEntryEncryptor<E> extends XWrappedEncryptor implements XEncryptor, XEntryFilter<E> {
    protected final Collection<? extends XEntryFilter<E>> filters;
    protected final XNopEncryptor xNopEncryptor = new XNopEncryptor();

    protected XEntryEncryptor(XEncryptor xEncryptor) {
        this(xEncryptor, null);
    }

    protected XEntryEncryptor(XEncryptor xEncryptor, Collection<? extends XEntryFilter<E>> filters) {
        super(xEncryptor);
        this.filters = filters != null ? filters : Collections.<XEntryFilter<E>>emptySet();
    }

    @Override
    public boolean filter(E entry) {
        for (XEntryFilter<E> filter : filters) {
            if (!filter.filter(entry)) {
                return false;
            }
        }
        return true;
    }
}
