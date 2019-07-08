package io.xjar;

/**
 * 记录可过滤的加密器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/23 20:38
 */
public abstract class XEntryEncryptor<E> extends XWrappedEncryptor implements XEncryptor, XEntryFilter<E> {
    protected final XEntryFilter<E> filter;
    protected final XNopEncryptor xNopEncryptor = new XNopEncryptor();

    protected XEntryEncryptor(XEncryptor xEncryptor, XEntryFilter<E> filter) {
        super(xEncryptor);
        this.filter = filter;
    }

    @Override
    public boolean filtrate(E entry) {
        return filter == null || filter.filtrate(entry);
    }

    protected static abstract class XEntryEncryptorBuilder<E, T extends XEntryEncryptor<E>, B extends XEntryEncryptorBuilder<E, T, B>> extends XWrappedEncryptorBuilder<T, B> {
        protected XEntryFilter<E> filter;

        public B filter(XEntryFilter<E> filter) {
            this.filter = filter;
            return (B) this;
        }
    }
}
