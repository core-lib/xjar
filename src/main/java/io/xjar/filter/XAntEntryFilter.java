package io.xjar.filter;

import io.xjar.XEntryFilter;

/**
 * Ant表达式过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 11:21
 */
public abstract class XAntEntryFilter<E> extends XRegexEntryFilter<E> implements XEntryFilter<E> {
    private static final String[] SYMBOLS = {"\\", "$", "(", ")", "+", ".", "[", "]", "^", "{", "}", "|"};

    protected XAntEntryFilter(String ant) {
        super(convert(ant));
    }

    /**
     * 将ANT风格路径表达式转换成正则表达式
     *
     * @param ant ANT风格路径表达式
     * @return 正则表达式
     */
    private static String convert(String ant) {
        String regex = ant;
        for (String symbol : SYMBOLS) regex = regex.replace(symbol, '\\' + symbol);
        regex = regex.replace("?", ".{1}");
        regex = regex.replace("**/", "(.{0,}?/){0,}?");
        regex = regex.replace("**", ".{0,}?");
        regex = regex.replace("*", "[^/]{0,}?");
        while (regex.startsWith("/")) regex = regex.substring(1);
        while (regex.endsWith("/")) regex = regex.substring(0, regex.length() - 1);
        return regex;
    }
}
