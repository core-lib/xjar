package io.xjar.boot;

import io.xjar.XJarArchiveEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Spring Boot 缺省的过滤器, 只加密本身项目的资源。
 *
 * @author Payne 646742615@qq.com
 * 2018/11/28 11:02
 */
public class XBootDefaultFilter implements XJarArchiveEntryFilter {

    @Override
    public boolean filter(JarArchiveEntry entry) {
        return entry.getName().startsWith("BOOT-INF/classes/");
    }

}
