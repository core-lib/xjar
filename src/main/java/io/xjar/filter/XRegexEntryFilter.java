package io.xjar.filter;

import io.xjar.XEntryFilter;

import java.util.regex.Pattern;

/**
 * 正则表达式过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 11:27
 */
public abstract class XRegexEntryFilter<E> implements XEntryFilter<E> {
    protected final Pattern pattern;

    protected XRegexEntryFilter(String regex) {
        this(Pattern.compile(regex));
    }

    protected XRegexEntryFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean filtrate(E entry) {
        String text = toText(entry);
        return pattern.matcher(text).matches();
    }

    /**
     * 将记录转换成字符串形式，用于模式匹配。
     *
     * @param entry 记录
     * @return 记录的字符串表达形式
     */
    protected abstract String toText(E entry);

}
