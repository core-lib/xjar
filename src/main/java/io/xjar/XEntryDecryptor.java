package io.xjar;

import java.util.Collection;
import java.util.Collections;

/**
 * 记录可过滤的解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 20:38
 */
public abstract class XEntryDecryptor<E> extends XWrappedDecryptor implements XDecryptor, XEntryFilter<E> {
    protected final Collection<? extends XEntryFilter<E>> filters;
    protected final XNopDecryptor xNopDecryptor = new XNopDecryptor();

    protected XEntryDecryptor(XDecryptor xDecryptor) {
        this(xDecryptor, null);
    }

    protected XEntryDecryptor(XDecryptor xDecryptor, Collection<? extends XEntryFilter<E>> filters) {
        super(xDecryptor);
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
