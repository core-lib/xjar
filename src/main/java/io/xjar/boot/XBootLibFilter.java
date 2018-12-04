package io.xjar.boot;

import io.xjar.XJarArchiveEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Spring-Boot项目lib依赖过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/29 9:40
 */
public class XBootLibFilter implements XJarArchiveEntryFilter {

    @Override
    public boolean filtrate(JarArchiveEntry entry) {
        return entry.getName().startsWith("BOOT-INF/lib/");
    }

}
