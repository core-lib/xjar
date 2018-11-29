package io.xjar.boot;

import io.xjar.XJarArchiveEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Spring-Boot项目所有资源过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/29 9:57
 */
public class XBootAllFilter implements XJarArchiveEntryFilter {

    @Override
    public boolean filter(JarArchiveEntry entry) {
        return entry.getName().startsWith("BOOT-INF/");
    }

}
