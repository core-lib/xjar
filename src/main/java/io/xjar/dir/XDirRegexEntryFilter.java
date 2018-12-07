package io.xjar.dir;

import io.xjar.XEntryFilter;
import io.xjar.filter.XRegexEntryFilter;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 文件记录正则表达式过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 12:04
 */
public class XDirRegexEntryFilter extends XRegexEntryFilter<File> implements XEntryFilter<File> {

    public XDirRegexEntryFilter(String regex) {
        super(regex);
    }

    public XDirRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(File entry) {
        return entry.getName();
    }
}
