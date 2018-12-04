package io.xjar.war;

import io.xjar.XEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Spring-Boot项目所有资源过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/29 9:57
 */
public class XWarAllFilter implements XEntryFilter<JarArchiveEntry> {

    @Override
    public boolean filtrate(JarArchiveEntry entry) {
        return entry.getName().startsWith("WEB-INF/classes/io/") || entry.getName().startsWith("WEB-INF/lib/");
    }

}
