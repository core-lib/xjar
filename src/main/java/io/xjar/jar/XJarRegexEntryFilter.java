package io.xjar.jar;

import io.xjar.XEntryFilter;
import io.xjar.filter.XRegexEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.util.regex.Pattern;

/**
 * Jar记录正则表达式过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 12:04
 */
public class XJarRegexEntryFilter extends XRegexEntryFilter<JarArchiveEntry> implements XEntryFilter<JarArchiveEntry> {

    public XJarRegexEntryFilter(String regex) {
        super(regex);
    }

    public XJarRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(JarArchiveEntry entry) {
        return entry.getName();
    }
}
