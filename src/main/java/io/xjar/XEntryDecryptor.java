package io.xjar;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 记录可过滤的解密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 20:38
 */
public abstract class XEntryDecryptor<E> extends XWrappedDecryptor implements XDecryptor, XEntryFilter<E> {
    protected final Collection<XEntryFilter<E>> filters;
    protected final XNopDecryptor xNopDecryptor = new XNopDecryptor();
    protected final XAlwaysFilter<E> xAlwaysFilter = new XAlwaysFilter<>();
    protected final XNeverFilter<E> xNeverFilter = new XNeverFilter<>();

    protected XEntryDecryptor(XDecryptor xDecryptor) {
        this(xDecryptor, (Collection<XEntryFilter<E>>) null);
    }

    protected XEntryDecryptor(XDecryptor xDecryptor, XEntryFilter<E>... filters) {
        this(xDecryptor, Arrays.asList(filters));
    }

    protected XEntryDecryptor(XDecryptor xDecryptor, Collection<XEntryFilter<E>> filters) {
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
