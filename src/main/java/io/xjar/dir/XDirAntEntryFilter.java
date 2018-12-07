package io.xjar.dir;

import io.xjar.XEntryFilter;
import io.xjar.filter.XAntEntryFilter;

import java.io.File;

/**
 * 文件记录Ant表达式过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/12/7 12:05
 */
public class XDirAntEntryFilter extends XAntEntryFilter<File> implements XEntryFilter<File> {

    public XDirAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(File entry) {
        return entry.getName();
    }

}
