package io.xjar.zip;

import io.xjar.XEntryFilter;
import io.xjar.filter.XRegexEntryFilter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.util.regex.Pattern;

/**
 * Zip记录正则表达式过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 12:04
 */
public class XZipRegexEntryFilter extends XRegexEntryFilter<ZipArchiveEntry> implements XEntryFilter<ZipArchiveEntry> {

    public XZipRegexEntryFilter(String regex) {
        super(regex);
    }

    public XZipRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(ZipArchiveEntry entry) {
        return entry.getName();
    }
}
