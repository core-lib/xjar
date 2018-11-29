package io.xjar.war;

import io.xjar.XJarArchiveEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Spring-Boot项目lib依赖过滤器
 *
 * @author Payne 646742615@qq.com
 * 2018/11/29 9:40
 */
public class XWarLibFilter implements XJarArchiveEntryFilter {

    @Override
    public boolean filter(JarArchiveEntry entry) {
        return entry.getName().startsWith("WEB-INF/lib/");
    }

}
