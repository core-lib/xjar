package io.xjar.jar;

import io.xjar.XEntryFilter;
import io.xjar.filter.XAntEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Jar记录Ant表达式过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 12:05
 */
public class XJarAntEntryFilter extends XAntEntryFilter<JarArchiveEntry> implements XEntryFilter<JarArchiveEntry> {

    public XJarAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(JarArchiveEntry entry) {
        return entry.getName();
    }

}
