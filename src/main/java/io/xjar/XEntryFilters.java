package io.xjar;

import java.util.Collection;

/**
 * 混合过滤器工具类
 *
 * @author Payne 646742615@qq.com
 * 2018/12/4 15:34
 */
public abstract class XEntryFilters {

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static <E> XAllEntryFilter<E> all() {
        return new XAllEntryFilter<>();
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static <E> XAllEntryFilter<E> all(Collection<? extends XEntryFilter<E>> filters) {
        return new XAllEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static <E> XAllEntryFilter<E> and() {
        return new XAllEntryFilter<>();
    }

    /**
     * 创建多个子过滤器AND连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器AND连接的混合过滤器
     */
    public static <E> XAllEntryFilter<E> and(Collection<? extends XEntryFilter<E>> filters) {
        return new XAllEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static <E> XAnyEntryFilter<E> any() {
        return new XAnyEntryFilter<>();
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static <E> XAnyEntryFilter<E> any(Collection<? extends XEntryFilter<E>> filters) {
        return new XAnyEntryFilter<>(filters);
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static <E> XAnyEntryFilter<E> or() {
        return new XAnyEntryFilter<>();
    }

    /**
     * 创建多个子过滤器OR连接的混合过滤器
     *
     * @param filters 子过滤器
     * @return 多个子过滤器OR连接的混合过滤器
     */
    public static <E> XAnyEntryFilter<E> or(Collection<? extends XEntryFilter<E>> filters) {
        return new XAnyEntryFilter<>(filters);
    }

}
