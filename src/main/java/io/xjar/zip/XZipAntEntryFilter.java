package io.xjar.zip;

import io.xjar.XEntryFilter;
import io.xjar.filter.XAntEntryFilter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

/**
 * Zip记录Ant表达式过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 12:05
 */
public class XZipAntEntryFilter extends XAntEntryFilter<ZipArchiveEntry> implements XEntryFilter<ZipArchiveEntry> {

    public XZipAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(ZipArchiveEntry entry) {
        return entry.getName();
    }

}
